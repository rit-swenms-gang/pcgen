package pcgen.cdom.converter;

import org.junit.jupiter.api.Test;
import pcgen.base.util.ObjectContainer;
import pcgen.cdom.base.Converter;
import pcgen.cdom.base.PrimitiveFilter;
import pcgen.core.PlayerCharacter;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class AddFilterConverterTest {
    // Dummy converter that applies filter logic to produce predictable outputs
    private static class DummyConverter implements Converter<String, String> {
        @Override
        public Collection<String> convert(ObjectContainer<String> orig) {
            return Collections.singletonList("converted");
        }

        @Override
        public Collection<String> convert(ObjectContainer<String> orig, PrimitiveFilter<String> filter) {
            if (filter.allow(null, "ok")) {
                return Collections.singletonList("filtered");
            }
            return Collections.emptyList();
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof DummyConverter; // all DummyConverters equal
        }

        @Override
        public int hashCode() {
            return 1;
        }
    }

    private static class TrueFilter implements PrimitiveFilter<String> {
        @Override
        public boolean allow(PlayerCharacter pc, String obj) {
            return true;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof TrueFilter;
        }

        @Override
        public int hashCode() {
            return 1;
        }
    }

    private static class FalseFilter implements PrimitiveFilter<String> {
        @Override
        public boolean allow(PlayerCharacter pc, String obj) {
            return false;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof FalseFilter;
        }

        @Override
        public int hashCode() {
            return 2;
        }
    }

    // Minimal stub implementing required ObjectContainer methods
    private static class DummyContainer implements ObjectContainer<String> {
    @Override
    public Collection<String> getContainedObjects() {
        return Collections.singletonList("x");
    }

    @Override
    public String getLSTformat(boolean verbose) {
        return "dummy";
    }

    @Override
    public Class<String> getReferenceClass() {
        return String.class;
    }

    @Override
    public boolean contains(String obj) {
        return "x".equals(obj);
    }
}

    @Test
    void testConvertWithoutLimit() {
        AddFilterConverter<String, String> conv =
                new AddFilterConverter<>(new DummyConverter(), new TrueFilter());
        Collection<? extends String> result = conv.convert(new DummyContainer());
        assertEquals(Collections.singletonList("filtered"), result);
    }

    @Test
    void testConvertWithLimit() {
        AddFilterConverter<String, String> conv =
                new AddFilterConverter<>(new DummyConverter(), new TrueFilter());
        Collection<? extends String> result = conv.convert(new DummyContainer(), new FalseFilter());
        // Filters disagree -> CompoundFilter will return false -> empty result
        assertTrue(result.isEmpty());
    }

    @Test
    void testEqualsAndHashCode() {
        AddFilterConverter<String, String> conv1 =
                new AddFilterConverter<>(new DummyConverter(), new TrueFilter());
        AddFilterConverter<String, String> conv2 =
                new AddFilterConverter<>(new DummyConverter(), new TrueFilter());
        AddFilterConverter<String, String> conv3 =
                new AddFilterConverter<>(new DummyConverter(), new FalseFilter());

        assertEquals(conv1, conv1);           // reflexive
        assertEquals(conv1, conv2);           // equal filters
        assertNotEquals(conv1, conv3);        // different filters
        assertEquals(conv1.hashCode(), conv2.hashCode());
    }

    @Test
    void testNotEqualsToOtherObject() {
        AddFilterConverter<String, String> conv =
                new AddFilterConverter<>(new DummyConverter(), new TrueFilter());
        assertNotEquals(conv, "some string");
        assertNotEquals(conv, null);
    }
}