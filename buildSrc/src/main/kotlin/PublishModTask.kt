/*
 * Copyright 2021 Michael Johnston
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import publish.RemoteMods
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.ObjectCannedACL.PUBLIC_READ
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import util.hash

private const val VPK_FILE = "pak01_dir.vpk"
private const val OUTPUT_VPK_FILE = "compiled/$VPK_FILE"
private const val MODS_BUCKET = "dota-mods"

private val s3 by lazy { S3Client.builder().region(Region.US_EAST_2).build() }

/** Upload the VPK file to S3 if it's been changed since the last upload. */
open class PublishModTask : DefaultTask() {

    @TaskAction
    fun run() {
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

        RemoteMods.updateModHash(modKey, localHash, (vpk.length() / 1024).toInt())
    }
}