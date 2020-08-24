package dk.mercell.playday.mamehighscoresapi.utils

import dk.mercell.playday.mamehighscoresapi.domain.GameHighscore
import dk.mercell.playday.mamehighscoresapi.domain.HighscoreEntry
import gse.hi2txt.CmdTxt
import gse.hi2txt.model.OutputData
import gse.hi2txt.model.OutputField
import gse.hi2txt.model.OutputTable
import java.math.BigInteger

object HighscoreUtils {

    fun readHighscores(path: String): List<HighscoreEntry> {
        val hiscoredatfile = "./data/hiscore.dat";
        val requestedDisplay = "always"
        val hiscore = CmdTxt(path, hiscoredatfile, null)
        val highscores = arrayListOf<HighscoreEntry>()
        val result = arrayListOf<GameHighscore>()
        if (hiscore.read(requestedDisplay)) {
            val mInputDatas = getInputDatas(hiscore)
            val mOutputTable = getOutputTable(hiscore)

            mInputDatas.get("SCORE")!!.forEachIndexed { index, _ ->
                val score = BigInteger(getFieldValue(mOutputTable.get("SCORE"), mInputDatas, index))
                val name = getFieldValue(mOutputTable.get("NAME"), mInputDatas, index)
                highscores.add(HighscoreEntry(score, name))
            }
        }
        return highscores;
    }

    private fun getFieldValue(mOutputField: OutputField?, mInputDatas: Map<String, List<Any>>, index: Int): String =
            OutputField.getValue(mOutputField, mInputDatas, index, index, null as String?).toString()

    private fun getInputDatas(hiscore: CmdTxt): Map<String, List<Any>> {
        val field = hiscore::class.java.getDeclaredField("mInputDatas")
        field.isAccessible = true
        val mInputDatas = field.get(hiscore) as Map<String, List<Any>>
        return mInputDatas
    }

    private fun getOutputTable(hiscore: CmdTxt): OutputTable {
        val field = hiscore::class.java.getDeclaredField("mOutputDatas")
        field.isAccessible = true
        val mOutputDatas = field.get(hiscore) as List<OutputData>
        return mOutputDatas.filterIsInstance<OutputTable>().first()
    }

}