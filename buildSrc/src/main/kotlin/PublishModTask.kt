import org.eclipse.jgit.api.Git
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.ObjectCannedACL.PUBLIC_READ
import software.amazon.awssdk.services.s3.model.PutObjectRequest

private const val VPK_FILE = "compiled/pak01_dir.vpk"
private const val META_FILE = "meta.txt"
private const val MODS_BUCKET = "dota-mods"

/** Upload the mod's VPK and info to S3. */
open class PublishModTask : DefaultTask() {
    var modName: String = ""
    var modDescription: String = ""

    @TaskAction
    fun run() {
        require(modName.isNotBlank()) { "Mod name must not be blank" }
        require(modDescription.isNotBlank()) { "Mod description must not be blank" }
        val vpk = project.file(VPK_FILE)
        require(vpk.exists()) { "VPK file does not exist" }

        if (!hasChanges()) {
            return
        }

        val s3 = S3Client.builder().region(Region.US_EAST_2).build()

        s3.putObject(
                PutObjectRequest.builder()
                        .bucket(MODS_BUCKET)
                        .acl(PUBLIC_READ)
                        .key("${project.name}/${vpk.name}")
                        .build(),
                RequestBody.fromFile(vpk)
        )
        s3.putObject(
                PutObjectRequest.builder()
                        .bucket(MODS_BUCKET)
                        .key("${project.name}/$META_FILE")
                        .build(),
                RequestBody.fromString("""
                    name=$modName
                    description=$modDescription
                """.trimIndent())
        )
    }

    private fun hasChanges(): Boolean {
        val repo = FileRepositoryBuilder()
                .findGitDir()
                .build()

        require(repo.branch == "main") {
            "On branch ${repo.branch} instead of main"
        }

        val git = Git.wrap(repo)
        val status = git.status().addPath("${project.name}/compiled").call()

        return !status.isClean
    }
}