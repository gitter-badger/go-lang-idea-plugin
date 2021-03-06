package ro.redeul.google.go.inspection;

import com.intellij.openapi.vfs.LocalFileSystem;
import org.junit.Ignore;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class FunctionCallInspectionTest extends GoInspectionTestCase {

    private boolean testDataFileExists(String fileName) {
        String absName = getTestDataPath() + File.separator + fileName;
        return LocalFileSystem.getInstance().findFileByPath(absName) != null;
    }

    @Override
    protected void doTest() throws Exception {
        List<String> files = new LinkedList<String>();
        if (testDataFileExists("builtin.go")) {
            files.add("builtin.go");
        }
        Collections.reverse(files);
        myFixture.configureByFiles(files.toArray(new String[files.size()]));
        super.doTest();
    }

    public void testSimple() throws Exception{ doTest(); }
    public void testMake() throws Exception{ doTest(); }
    public void testNew() throws Exception{ doTest(); }

/*    @Ignore("not ready yet")
    public void testFuncCall() throws Exception{ doTest(); }

    @Ignore("not ready yet")
    public void testBuiltinCall() throws Exception{ doTest(); }*/
}
