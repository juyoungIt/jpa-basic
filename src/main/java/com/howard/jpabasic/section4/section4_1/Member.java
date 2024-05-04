package com.howard.jpabasic.section4.section4_1;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity              // 기본값은 클래스명을 그대로 사용
@Table(name = "MBR") // Table 명을 커스텀하고 싶은 경우
public class Member {

    @Id @GeneratedValue
    private Long id;

    /* 회원이름이 고유해야하며, 5자 이하여야 함 */
    @Column(name = "name", unique = true, length = 5)
    private String username;

    private int age;

}
