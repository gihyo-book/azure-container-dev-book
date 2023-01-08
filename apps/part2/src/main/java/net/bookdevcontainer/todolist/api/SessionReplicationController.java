// package net.bookdevcontainer.todolist.api;

// import org.springframework.context.annotation.Bean;
// import org.springframework.session.data.redis.config.ConfigureRedisAction;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import javax.servlet.http.HttpSession;

// @RestController
// @RequestMapping("/")
// public class SessionReplicationController {

//     @Bean
//     public static ConfigureRedisAction configureRedisAction() {
//         return ConfigureRedisAction.NO_OP;
//     }

//     @GetMapping("/session")
//     public String session(HttpSession session) {
//         Integer test = (Integer) session.getAttribute("test");
//         if (test == null) {
//             test = 0;
//         } else {
//             test++;
//         }
//         session.setAttribute("test", test);
//         return "[" + session.getId() + "]-" + test;
//     }
// }