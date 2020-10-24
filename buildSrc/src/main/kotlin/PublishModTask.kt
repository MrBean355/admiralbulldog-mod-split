import org.eclipse.jgit.api.Git
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request
import software.amazon.awssdk.services.s3.model.ObjectCannedACL.PUBLIC_READ
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import util.GitHubService

private const val VPK_FILE = "pak01_dir.vpk"
private const val OUTPUT_VPK_FILE = "compiled/$VPK_FILE"
private const val META_FILE = "meta.txt"
private const val MODS_BUCKET = "dota-mods"

private val s3 by lazy { S3Client.builder().region(Region.US_EAST_2).build() }

/** Upload the mod's VPK and info to S3. */
open class PublishModTask : DefaultTask() {
    var modName: String = ""
    var modDescription: String = ""

    @TaskAction
    fun run() {
        require(modName.isNotBlank()) { "Mod name must not be blank" }
        require(modDescription.isNotBlank()) { "Mod description must not be blank" }
        val vpk = project.file(OUTPUT_VPK_FILE)
        require(vpk.exists()) { "VPK file does not exist" }
        val modKey = project.name

        if (!hasChanges(modKey) && RemoteMods.exists(modKey)) {
            return
        }

        s3.putObject(
                PutObjectRequest.builder()
                        .bucket(MODS_BUCKET)
                        .acl(PUBLIC_READ)
                        .key("$modKey/${vpk.name}")
                        .build(),
                RequestBody.fromFile(vpk)
        )
        s3.putObject(
                PutObjectRequest.builder()
                        .bucket(MODS_BUCKET)
                        .key("$modKey/$META_FILE")
                        .build(),
                RequestBody.fromString("""
                    name=$modName
                    description=$modDescription
                """.trimIndent())
        )
        refreshMods()
    }

    private fun hasChanges(modKey: String): Boolean {
        val repo = FileRepositoryBuilder()
                .findGitDir()
                .build()

        require(repo.branch == "main") {
            "On branch ${repo.branch} instead of main"
        }

        val git = Git.wrap(repo)
        val status = git.status().addPath("$modKey/compiled").call()

        return !status.isClean
    }

    private fun refreshMods() {
        val token = System.getenv("AUTH_TOKEN")
        require(!token.isNullOrBlank()) {
            "No token provided via AUTH_TOKEN"
        }
        GitHubService.INSTANCE.refreshMods(token).execute()
    }
}

/** Remember which mods exist on S3. */
private object RemoteMods {
    private val mods: Set<String>

    init {
        val response = s3.listObjectsV2(ListObjectsV2Request.builder().bucket(MODS_BUCKET).build())
        mods = response.contents().map { it.key() }
                .filter { it.endsWith(VPK_FILE) }
                .map { it.substringBefore('/') }
                .toSet()
    }

    fun exists(name: String): Boolean {
        return name in mods
    }
}