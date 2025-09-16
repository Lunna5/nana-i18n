package dev.lunna.nana.translator.test;

import dev.lunna.nana.translator.script.ScriptParser;
import org.junit.jupiter.api.Test;

import java.util.Locale;

public class ScriptParserTest {
    private static final String SCRIPT_1 = """
            	EXECMODE ( 0 )
            	BGMPLAY ( 2 )
            	BGMCHVOL ( 20 ) ( 0 )
            	EVTBGA ( 2 ) ( 148 + 38 + 15 )
            	EVTFADE ( 0 )
            	EVTMESBG ( 0 )
            	MSGN 53653 "なな" "（あ、そろそろ終業時間だ①ｎ帰る用意しちゃおうっと）"
            	WAIT ( 20 )
            	EVTCH ( 0 ) ( 137 )
            	WAIT ( 20 )
            	MSGN 4467 "坂上" "ちょっとあなた①ｎなに帰ろうとしてるのかしら"
            	MSGN 4468 "坂上" "今、あなたが投げ出そうとしている仕事①ｎ急ぎの仕事なんだからね"
            	MSGN 4469 "坂上" "終わるまで帰らせないわよ"
            	EVTSELMSGN ( 2 ) ( 0 ) 53654 "なな" "（ハァ～、残業かぁ……）" "残業する" "帰っちゃう" ""
            	PUSH 0 // L0  int nAns
            	GETDATA ( 21 ) L0
            	OPE R0 = ( L0 )
            	JMP @LL_SW_001
            		@LL_SW_002
            		MSGN 53656 "なな" "（急ぎかぁ…①ｎしょーがない、頑張って終わらせよっ）"
            		SETDATA ( 23 ) ( 1 )
            		ADDTSUBONE ( -10 )
            		JMP @LL_SW_000
            		@LL_SW_003
            		MSGN 53655 "なな" "（でも、残業って気分じゃないんだよね①ｎ帰っちゃおうっと！）"
            		SETDATA ( 23 ) ( 2 )
            		ADDTSUBONE ( 40 )
            		JMP @LL_SW_000
            	JMP @LL_SW_000
            	@LL_SW_001
            		OPE R1 = ( ( 0 ) == R0 )
            		IF R1 JMP @LL_SW_002
            		OPE R1 = ( ( 1 ) == R0 )
            		IF R1 JMP @LL_SW_003
            	@LL_SW_000
            	WAIT ( 50 )
            	BGMSTOP ( 60 )
            	EVTFADE ( 1 )
            	EVTMESBG ( 3 )
            	WAIT ( 50 )
            	EXIT
            EOF            
            """;

    private static String SCRIPT_2 = """
            EXECMODE ( 4 )
            PUSH ( 10 ) // G0  int day
            PUSH ( 2 ) // G1  int place
            GETDATA ( 13 ) G0
            GETDATA ( 12 ) G1
            SETSTR ( 3 ) ( 1 ) ( G0 )
            SETSTR ( 2 ) ( 2 ) ( G1 )
            MSG ( 0 ) "淳子です"
            MSG ( 0 ) "①Ｓ１、時間とれる？"
            MSG ( 0 ) "昨日①Ｓ２でななの好きそうな感じの店見つけたけど一緒にどう？"
            EXIT
            EOF
            """;

    @Test
    public void testScript1() {
        ScriptParser parser = new ScriptParser();
        parser.parse(SCRIPT_1, "mem:SCRIPT_1");
        parser.getLines();

        int[] hashes = new int[parser.getLines().size()];

        for (int i = 0; i < parser.getLines().size(); i++) {
            hashes[i] = parser.getLines().get(i).hashCode();
        }

        for (var line : parser.getLines()) {
            line.setText(line.getText().toUpperCase(Locale.JAPAN));
        }

        for (int i = 0; i < parser.getLines().size(); i++) {
            System.out.println(i + ": " + parser.getLines().get(i).getText() + " == " + hashes[i] + " ? " + (parser.getLines().get(i).hashCode() == hashes[i]));
        }
    }

    @Test
    public void testScript2() {
        ScriptParser parser = new ScriptParser();
        parser.parse(SCRIPT_2, "mem:SCRIPT_2");
        parser.getLines();
        for (int i = 0; i < parser.getLines().size(); i++) {
            System.out.println(i + ": " + parser.getLines().get(i).hashCode());
        }
    }
}
