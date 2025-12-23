package com.hamet.profile.entity;

import java.time.LocalDate;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Khai báo entity UserProfile dưới dạng một nút trong cơ sở dữ liệu Neo4j
 * Đánh dấu class này là 1 nút (Node) trong sơ đồ đồ thị. 
 * Thay vì lưu dạng bảng như MySQL(Entity), nó lưu dạng đường nối (Relationship) giữa các nút.
 */
@Node("user_profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(
    level = lombok.AccessLevel.PRIVATE// Tác dụng: tất cả các trường trong class này đều có mức truy cập là private
) 
@Builder
public class UserProfile {
    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    String id;

    @Property("user_id") // Thuộc tính userId trong nút name: user_profile
    String userId; // Relationship với id của User trong identity-service

    @Property("first_name")
    String firstName;

    @Property("last_name")
    String lastName;

    @Property("dob")
    LocalDate dob;

    @Property("city")
    String city;
}
