package com.provectus.kafka.ui.tests;

import com.provectus.kafka.ui.base.BaseTest;
import com.provectus.kafka.ui.helpers.Helpers;
import com.provectus.kafka.ui.pages.MainPage;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

public class TopicTests extends BaseTest {

    public static final String NEW_TOPIC = "new-topic";
    public static final String TOPIC_TO_UPDATE = "topic-to-update";
    public static final String TOPIC_TO_DELETE = "topic-to-delete";
    public static final String SECOND_LOCAL = "secondLocal";
    public static final String COMPACT_POLICY_VALUE = "Compact";
    public static final String UPDATED_TIME_TO_RETAIN_VALUE = "604800001";
    public static final String UPDATED_MAX_SIZE_ON_DISK = "20 GB";
    public static final String UPDATED_MAX_MESSAGE_BYTES = "1000020";

    @BeforeAll
    @SneakyThrows
    public static void beforeAll() {
        Helpers.INSTANCE.apiHelper.createTopic(SECOND_LOCAL, TOPIC_TO_UPDATE);
        Helpers.INSTANCE.apiHelper.createTopic(SECOND_LOCAL, TOPIC_TO_DELETE);
    }

    @AfterAll
    @SneakyThrows
    public static void afterAll() {
        Helpers.INSTANCE.apiHelper.deleteTopic(SECOND_LOCAL, TOPIC_TO_UPDATE);
        Helpers.INSTANCE.apiHelper.deleteTopic(SECOND_LOCAL, TOPIC_TO_DELETE);
        Helpers.INSTANCE.apiHelper.deleteTopic(SECOND_LOCAL, NEW_TOPIC);
    }

    @SneakyThrows
    @DisplayName("should create a topic")
    @Test
    void createTopic() {
        helpers.apiHelper.createTopic(SECOND_LOCAL, NEW_TOPIC);
        pages.open()
                .goToSideMenu(SECOND_LOCAL, MainPage.SideMenuOptions.TOPICS)
                .topicIsVisible(NEW_TOPIC);
        helpers.apiHelper.deleteTopic(SECOND_LOCAL, NEW_TOPIC);
        pages.open()
                .goToSideMenu(SECOND_LOCAL, MainPage.SideMenuOptions.TOPICS)
                .topicIsNotVisible(NEW_TOPIC);
    }

    @SneakyThrows
    @DisplayName("should update a topic")
    @Test
    void updateTopic() {
        pages.openTopicsList(SECOND_LOCAL)
                .isOnPage()
                .openTopic(TOPIC_TO_UPDATE)
                .openEditSettings()
                .selectCleanupPolicy(COMPACT_POLICY_VALUE)
                .setTimeToRetainDataInMs(UPDATED_TIME_TO_RETAIN_VALUE)
                .setMaxSizeOnDiskInGB(UPDATED_MAX_SIZE_ON_DISK)
                .setMaxMessageBytes(UPDATED_MAX_MESSAGE_BYTES)
                .sendData()
                .isOnTopicViewPage();
        pages.openTopicView(SECOND_LOCAL, TOPIC_TO_UPDATE)
                .openEditSettings()
                // Assertions
                .cleanupPolicyIs(COMPACT_POLICY_VALUE)
                .timeToRetainIs(UPDATED_TIME_TO_RETAIN_VALUE)
                .maxSizeOnDiskIs(UPDATED_MAX_SIZE_ON_DISK)
                .maxMessageBytesIs(UPDATED_MAX_MESSAGE_BYTES);
    }

    @SneakyThrows
    @DisplayName("should delete topic")
    @Test
    void deleteTopic() {
        pages.openTopicsList(SECOND_LOCAL)
                .isOnPage()
                .openTopic(TOPIC_TO_DELETE);
        pages.openTopicView(SECOND_LOCAL, TOPIC_TO_DELETE)
                .deleteTopic();
        pages.openTopicsList(SECOND_LOCAL)
                .isTopicNotVisible(TOPIC_TO_DELETE);
    }
}
