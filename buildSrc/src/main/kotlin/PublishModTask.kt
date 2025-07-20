import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import publish.RemoteMods
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.ObjectCannedACL.PUBLIC_READ
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import util.Environment
import util.hash

private const val VPK_FILE = "pak01_dir.vpk"
private const val OUTPUT_VPK_FILE = "compiled/$VPK_FILE"
private const val MODS_BUCKET = "dota-mods"

private val s3 by lazy { S3Client.builder().region(Region.US_EAST_2).build() }

/** Upload the VPK file to S3 if it's been changed since the last upload. */
open class PublishModTask : DefaultTask() {

    @TaskAction
    fun run() {
        Environment.init(project.properties)
        val vpk = project.file(OUTPUT_VPK_FILE)
        require(vpk.exists()) { "VPK file does not exist" }
        val localHash = vpk.hash()

        val modKey = project.name
        val remoteMod = RemoteMods.getMod(modKey)

        if (localHash == remoteMod.hash) {
            println("No changes made; not uploading.")
            return
        }

        println("Changes made; uploading...")
        s3.putObject(
            PutObjectRequest.builder()
                .bucket(MODS_BUCKET)
                .acl(PUBLIC_READ)
                .key("$modKey/${vpk.name}")
                .build(),
            RequestBody.fromFile(vpk)
        )

        val message = if (Environment.sendMessage) {
            val friendlyName = remoteMod.name.removeSuffix(" mod")
            "The <b>$friendlyName</b> mod has been updated to work with the latest Dota 2 update."
        } else null

        RemoteMods.updateModHash(modKey, localHash, (vpk.length() / 1024).toInt(), message)
    }
}