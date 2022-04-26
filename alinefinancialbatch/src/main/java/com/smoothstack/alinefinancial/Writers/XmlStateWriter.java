package com.smoothstack.alinefinancial.Writers;

import com.smoothstack.alinefinancial.Caches.StateCache;
import com.smoothstack.alinefinancial.Caches.UserCache;
import com.smoothstack.alinefinancial.Models.State;
import com.smoothstack.alinefinancial.Models.User;
import com.thoughtworks.xstream.XStream;
import org.springframework.batch.item.support.AbstractItemStreamItemWriter;

import java.io.FileOutputStream;
import java.util.List;

public class XmlStateWriter extends AbstractItemStreamItemWriter<State> {

    private final StateCache stateCache = StateCache.getInstance();

    public void setStateCache(StateCache statecache) {

    }

    @Override
    public void write(List items) throws Exception {
        XStream statesXStream = new XStream();
        statesXStream.processAnnotations(State.class);
        statesXStream.alias("state", State.class);
        FileOutputStream statesFs = new FileOutputStream("src/main/ProcessedOutFiles/XmlProcessedStates2.xml", true);

        stateCache.getSeenStates().forEach((k, v) -> {
            statesXStream.toXML(v, statesFs);
        });

    }
}
