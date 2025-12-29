package com.hamet.file.dto.response;

import org.springframework.core.io.Resource;

/**
 * Mục đích chính của nó là giúp code ngắn gọn hơn bằng cách loại bỏ các đoạn code "rác" 
 * (boilerplate) mà trước đây bạn phải viết thủ công hoặc dùng Lombok.
 * 
 * Mục đích
 *  1. Các Fields (thuộc tính) đều là private final (dữ liệu không thể thay đổi sau khi tạo - Immutable).
 *  2. Constructor để gán giá trị cho tất cả các fields.
 *  3. Các phương thức Getter (nhưng không có chữ "get", ví dụ: fileData.name() thay vì getName()).
 */
public record FileData(String contentType, Resource resource) {}
