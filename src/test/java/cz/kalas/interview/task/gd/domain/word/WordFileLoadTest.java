package cz.kalas.interview.task.gd.domain.word;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

class WordFileLoadTest {

    public static String testFileName = "test_file";

    @AfterEach
    public void tearDown() {
        new File(testFileName).delete();
    }

    @Test
    public void loadWordsTest() throws IOException {
        var testFileContent = "foo;adjective\nbull;unknown\nbar;verb";
        Files.write(Paths.get("./" + testFileName), testFileContent.getBytes());

        var result = new WordFileLoad(new File(testFileName)).getWords();
        assertThat(result, hasSize(2));
    }

}