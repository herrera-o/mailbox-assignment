package dev.herrerao;

import java.time.LocalDateTime;

public record Message(int messageID, int senderID, String senderName, String subject, String body,
                      LocalDateTime sent_at, Boolean is_read) {}