package com.coworking.memberservice.service;

import com.coworking.memberservice.model.Member;
import com.coworking.memberservice.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));
    }

    public Member create(Member member) {
        member.setSuspended(false);
        return memberRepository.save(member);
    }

    public Member update(Long id, Member memberDetails) {
        Member member = findById(id);
        member.setFullName(memberDetails.getFullName());
        member.setEmail(memberDetails.getEmail());
        member.setSubscriptionType(memberDetails.getSubscriptionType());
        return memberRepository.save(member);
    }

    public void delete(Long id) {
        memberRepository.deleteById(id);
    }

    public void suspend(Long id) {
        Member member = findById(id);
        member.setSuspended(true);
        memberRepository.save(member);
    }

    public void unsuspend(Long id) {
        Member member = findById(id);
        member.setSuspended(false);
        memberRepository.save(member);
    }
}
