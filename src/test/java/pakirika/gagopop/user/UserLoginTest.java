package pakirika.gagopop.user;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserLoginTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSave(){
        User user = new User();
        user.setName( "test" );
        user.setEmail( "test@gmail.com" );
        user.setRole(Role.USER );
        User savedUser=userRepository.save( user );

        Assertions.assertThat( savedUser ).isEqualTo( user );

    }

}
