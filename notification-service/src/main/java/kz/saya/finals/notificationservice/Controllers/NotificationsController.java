package kz.saya.finals.notificationservice.Controllers;

import kz.saya.finals.common.DTOs.GamerProfileDto;
import kz.saya.finals.common.DTOs.TeamDto;
import kz.saya.finals.common.DTOs.TeamMemberDto;
import kz.saya.finals.common.DTOs.UserDTO;
import kz.saya.finals.common.Enums.NotificationType;
import kz.saya.finals.feigns.Clients.GamerProfileServiceClient;
import kz.saya.finals.feigns.Clients.UserServiceClient;
import kz.saya.finals.notificationservice.DTOs.NotificationRequestDto;
import kz.saya.finals.notificationservice.Entity.Notification;
import kz.saya.finals.notificationservice.Entity.NotificationLink;
import kz.saya.finals.notificationservice.Repository.NotificationLinkRepository;
import kz.saya.finals.notificationservice.Service.NotificationService;
import lombok.Data;
import org.aspectj.weaver.ast.Not;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
public class NotificationsController {
    private final UserServiceClient userServiceClient;
    private final GamerProfileServiceClient gamerProfileServiceClient;
    private final NotificationService notificationService;
    private final NotificationLinkRepository notificationLinkRepository;

    public NotificationsController(UserServiceClient userServiceClient, GamerProfileServiceClient gamerProfileServiceClient, NotificationService notificationService, NotificationLinkRepository notificationLinkRepository) {
        this.userServiceClient = userServiceClient;
        this.gamerProfileServiceClient = gamerProfileServiceClient;
        this.notificationService = notificationService;
        this.notificationLinkRepository = notificationLinkRepository;
    }

    @PostMapping("/send")
    @PreAuthorize("hasAnyRole('NOTIFICATION_SEND', 'ADMIN')")
    public ResponseEntity<String> post(@RequestBody NotificationRequestDto request) {
        // Value - Текст уведомления
        UserDTO user = getCurrentUser();
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        System.out.println(user.getRoles());
        List<String> roles = user.getRoles();
        if (roles.contains("ROLE_ADMIN")) {
            List<NotificationLink> notificationLinks = notificationLinkRepository.findAllByUserIdInAndTypeEquals(request.getRecipient(), request.getType());
            if (notificationLinks.isEmpty()) {
                return ResponseEntity.status(404).body("None of the recipients have notifications turned on");
            }
            for (NotificationLink notificationLink : notificationLinks) {
                Notification notification = new Notification();
                notification.setType(request.getType());
                notification.setRecipient(notificationLink);
                notification.setSubject(request.getSubject());
                notification.setContent(request.getContent());
                notificationService.sendNotification(notification);
            }
            return ResponseEntity.ok("Notification sent to all users");
        }
        GamerProfileDto gamerProfile = getCurrentUserProfile();
        if (gamerProfile == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        // Отправляем уведомление только тем кому было задано, и то смотрим на права
        // Если создатель команды - то разрешено отправить только тем кто в команде
        List<TeamDto> teams = gamerProfileServiceClient.getTeamsByMember(gamerProfile.getId());
        if (teams.isEmpty()) {
            return ResponseEntity.status(403).body("Forbidden");
        }
        TeamDto team = teams.get(0);
        List<UUID> teamMemberUserIDs = team.getMembers().stream().map(TeamMemberDto::getUserId).toList();
        if (team.getCaptainId() != null && !team.getCaptainId().equals(gamerProfile.getId())) {
            return ResponseEntity.status(403).body("Forbidden");
        }
        if (team.getCreatorId() != null && !team.getCreatorId().equals(gamerProfile.getId())) {
            return ResponseEntity.status(403).body("Forbidden");
        }
        List<NotificationLink> notificationLinks = notificationLinkRepository.findAllByUserIdInAndTypeEquals(teamMemberUserIDs, request.getType());
        if (notificationLinks.isEmpty()) {
            return ResponseEntity.status(404).body("None of the team members have notifications turned on");
        }
        for (NotificationLink notificationLink : notificationLinks) {
            Notification notification = new Notification();
            notification.setType(request.getType());
            notification.setRecipient(notificationLink);
            notification.setContent(request.getContent());
            notificationService.sendNotification(notification);
        }
        return ResponseEntity.ok("Notification sent to team members");
    }

    @PostMapping("/email/link")
    public ResponseEntity<String> linkEmailAddress(@RequestParam String email) {
        UserDTO user = getCurrentUser();
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        NotificationLink notificationLink = new NotificationLink();
        notificationLink.setUserId(user.getId());
        notificationLink.setType(NotificationType.EMAIL);
        notificationLink.setLink(email);
        notificationLinkRepository.save(notificationLink);
        return ResponseEntity.ok("Email linked successfully");
    }

    @PostMapping("/telegram/link")
    public ResponseEntity<String> linkTelegram() {
        //TODO: Сделать, но не представляю как можно, так как нужно чтобы User переходит по ссылке в бота
        // Ссылка должна иметь какой то параметр, и при старте бота автоматически его линковать
        return ResponseEntity.ok("NOT IMPLEMENTED YET!");
    }


    private UserDTO getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }
        String login = auth.getName();
        return userServiceClient.getByLogin(login);
    }

    private GamerProfileDto getCurrentUserProfile() {
        UserDTO user = getCurrentUser();
        if (user == null) {
            return null;
        }
        return gamerProfileServiceClient.getProfileByLogin(user.getLogin());
    }

}
