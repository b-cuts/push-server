package com.cosmicpush.pub.lqnotify;

public final class LqAttachment {

  private final String name;
    private final String contentType;
    private final byte[] content;

    public LqAttachment(String name,
                        String contentType,
                        byte[] content) {

        this.name = (name != null) ? name : "no name";
        this.contentType = (contentType != null) ? contentType : "unknown";
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public String getContentType() {
        return contentType;
    }

    public byte[] getContent() {
        return content;
    }
}
