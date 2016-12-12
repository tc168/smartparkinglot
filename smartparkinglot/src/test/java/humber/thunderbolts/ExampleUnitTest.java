package humber.thunderbolts;

import org.junit.Test;

import java.util.concurrent.ExecutionException;

import humber.thunderbolts.parking.ConnectDatabase;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    //This test is not possible due to limitation that junit does not suppor Async task.
    @Test
    public void connectToDataBase() throws ExecutionException, InterruptedException {
        ConnectDatabase con = new ConnectDatabase();
        CurrentThreadExecutor myAsyncTask = new CurrentThreadExecutor();

      //  myAsyncTask.executeOnExecutor(new CurrentThreadExecutor(), testParam);


        assertTrue(0 < con.executeOnExecutor(new CurrentThreadExecutor()).get().size());
    }
}

