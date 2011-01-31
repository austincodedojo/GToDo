package my.apps;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.database.Cursor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ControllerTest {
    private Controller controller;

    @Mock
    private LocalRepository localRepository;
    @Mock
    private Cursor cursor;
    @Mock
    private Synchronizer synchronizer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        controller = new Controller(localRepository, synchronizer);
    }

    @Test
    public void testImmediateResult() {
        String[] expectedProjection = {Lists.NAME};
        String expectedSortOrder = Lists.NAME + " asc";

        when(localRepository.getLists(expectedProjection, expectedSortOrder)).thenReturn(cursor);

        CursorResults results = controller.getTaskLists(expectedProjection, expectedSortOrder);
        assertEquals(cursor, results.getImmediate());
    }

    @Test
    public void testDeferredResult() {
        String[] expectedProjection = {Lists.NAME};
        String expectedSortOrder = Lists.NAME + " asc";

        when(localRepository.getLists(expectedProjection, expectedSortOrder)).thenReturn(cursor);

        CursorResults results = controller.getTaskLists(expectedProjection, expectedSortOrder);
        assertEquals(cursor, results.getDeferred());

        InOrder inOrder = inOrder(synchronizer, localRepository);
        inOrder.verify(synchronizer).synchronizeLists();
        inOrder.verify(localRepository).getLists(expectedProjection, expectedSortOrder);
    }
}
