package dk.mercell.playday.mamehighscoresapi.service

import dk.mercell.playday.mamehighscoresapi.domain.HighscoreEntry
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.io.File
import java.io.IOException
import java.math.BigInteger
import java.nio.file.attribute.FileTime
import java.util.*
import java.util.concurrent.TimeUnit

@Service
class Hi2TxtService {

    @Cacheable("files", key = "#file.absolutePath + #lastModifiedTime")
    fun readHighscores(file: File, lastModifiedTime: FileTime): List<HighscoreEntry> {
        val output = executeHi2txt(file.absolutePath)

        if (output != null) {
            return parseOutput(output)
        }

        return arrayListOf()
    }

    private fun executeHi2txt(path: String): String? {
        try {
            val proc = ProcessBuilder("java",
                    "-jar", "lib/hi2txt.jar",
                    "-hiscoredat", "./data/hiscore.dat",
                    "-r", path,
                    "-keep-field", "RANK",
                    "-keep-field", "SCORE",
                    "-keep-field", "NAME"
            )
                    .redirectOutput(ProcessBuilder.Redirect.PIPE)
                    .redirectError(ProcessBuilder.Redirect.PIPE)
                    .start()

            proc.waitFor(1000, TimeUnit.MILLISECONDS)

            return proc.inputStream.bufferedReader().readText()

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    private fun parseOutput(output: String): List<HighscoreEntry> {
        val highscores = arrayListOf<HighscoreEntry>();

        val scanner = Scanner(output)
        while (scanner.hasNext()) {
            val entry = parseLine(scanner.nextLine())
            if (entry != null) {
                highscores.add(entry)
            }
        }
        scanner.close()

        return highscores
    }

    private fun parseLine(line: String?): HighscoreEntry? {
        if (line != null && !"RANK|SCORE|NAME".equals(line)) {
            val parts = line.split("|")
            if (parts.size == 3) {
                return HighscoreEntry(
                        BigInteger(parts[1]),
                        parts[2]
                )
            }
        };
        return null
    }

}