package kz.saya.finals.teamservice.Repositories;

import kz.saya.finals.teamservice.Entities.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TeamMemberRepository extends JpaRepository<TeamMember, UUID> {
  }