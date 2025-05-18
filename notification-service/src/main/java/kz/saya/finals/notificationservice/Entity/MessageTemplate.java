package kz.saya.finals.notificationservice.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import kz.saya.sbasecore.Entity.MappedSuperClass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "message_template")
@Getter
@Setter
public class MessageTemplate extends MappedSuperClass {
    private String name;
    private String subject; // для email
    private String body;    // может содержать плейсхолдеры вроде {{username}}
}
