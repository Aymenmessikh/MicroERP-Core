package com.example.notificationservice.Service;
import com.example.notificationservice.Entity.User;
import com.example.notificationservice.Listner.Modele.UserEvent;
import com.example.notificationservice.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public void saveUser(UserEvent userEvent){
        User user= UserEvent.EntityFromUserListner(userEvent);
        userRepository.save(user);
    }
}
