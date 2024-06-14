package event;

import com.boruomi.BoruomiApplication;
import com.boruomi.business.model.entity.SysUserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(classes = BoruomiApplication.class)
@ExtendWith(SpringExtension.class)
public class EventTest {
    private final ApplicationEventPublisher eventPublisher;
    @Autowired
    public EventTest(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
    @Test
    public void getOne() {
        SysUserEntity user = new SysUserEntity();
        user.setUserName("小明");
        user.setAccount("xiaoming");
        System.out.println(Thread.currentThread().getName());
        eventPublisher.publishEvent(user);
    }
}
