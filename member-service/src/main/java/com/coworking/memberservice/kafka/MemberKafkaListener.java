package com.coworking.memberservice.kafka;

import com.coworking.memberservice.service.MemberService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MemberKafkaListener {

    private final MemberService memberService;

    public MemberKafkaListener(MemberService memberService) {
        this.memberService = memberService;
    }

    @KafkaListener(topics = "member-suspended", groupId = "member-service")
    public void onMemberSuspended(String memberId) {
        memberService.suspend(Long.parseLong(memberId));
    }

    @KafkaListener(topics = "member-unsuspended", groupId = "member-service")
    public void onMemberUnsuspended(String memberId) {
        memberService.unsuspend(Long.parseLong(memberId));
    }
}
