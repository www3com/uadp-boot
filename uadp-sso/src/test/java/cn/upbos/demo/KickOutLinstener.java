package cn.upbos.demo;

import com.upbos.sso.entity.Token;
import com.upbos.sso.storage.SessionKickOutListener;
import org.springframework.stereotype.Component;

@Component
public class KickOutLinstener implements SessionKickOutListener {
    @Override
    public void notice(Token token) {
        System.out.printf("session 被踢了：UID： %s", token.getUid());
    }
}
