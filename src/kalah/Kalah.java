package kalah;

import com.qualitascorpus.testsupport.IO;
import com.qualitascorpus.testsupport.MockIO;
import kalah.engine.KalahEngine;
import kalah.view.BoardRender;

/**
 * This class is the starting point for a Kalah implementation using
 * the test infrastructure.
 */
public class Kalah {

    public static void main(String[] args) {
        new Kalah().play(new MockIO());
    }

    public void play(IO io) {
        KalahEngine kalahEngine = new KalahEngine(new BoardRender(io));
        kalahEngine.play();
    }
}
