package study.data_jpa.dto;

import lombok.Data;

@Data
public class MemberDto {

    private Long id;
    private String username;
    private int age;
    private String teamname;

    public MemberDto(Long id, String username, int age, String teamname) {
        this.id = id;
        this.username = username;
        this.age = age;
        this.teamname = teamname;
    }
}
