package com.tqa.adl.sampletest;

import org.citrusframework.dsl.testng.TestNGCitrusTestRunner;
import org.testng.annotations.Test;

import com.consol.citrus.annotations.CitrusTest;

public class AldAutomationScriptTestsSSHVerify extends TestNGCitrusTestRunner {
    /*
     * Допустим некий таймер на запуск скрипта, еще не сработал.
     * Допустим есть некая команда для форсированного запуска задания, без ожидания
     * таймера.
     * Допустим есть команда для проверки конфигурации хоста.
     * Допустим что задание должно изменить имя хоста на aldhost.
     */

    private static final String[] HOSTS = { "host1", "host2", "host3" };
    private static final String COMMAND_RUN_TASK = "ald automation-task run --force";
    private static final String COMMAND_CHECK_CONFIGURATION = "ald host configuration --show";
    private static final String EXPECTED_CHECK_RESPONSE = "host-name = aldhost";

    @Test
    @CitrusTest

    public void testAutomationTasksOnHosts() {
        for (String host : HOSTS) {
            // Подключение к хосту
            connectToHost(host);

            // Запуск форсированного выполнения задания
            runAutomationTask(host);

            // Проверка конфигурации
            checkHostConfiguration(host);
        }
    }

    private void connectToHost(String host) {
        echo(String.format("Подключение по SSH к хосту: %s", host));

        send(sendMessageBuilder -> sendMessageBuilder.endpoint("sshClient")
                .payload(String.format(
                        "<ssh-request xmlns=\"http://www.citrusframework.org/schema/ssh/message\">" +
                                "<command>echo Connection to %s successful</command>" +
                                "</ssh-request>",
                        host)));
        receive(receiveMessageBuilder -> receiveMessageBuilder.endpoint("sshClient")
                .payload("<ssh-response xmlns=\"http://www.citrusframework.org/schema/ssh/message\">" +
                        "<stdout>Connection to " + host + " successful</stdout>" +
                        "<stderr/>" +
                        "<exit>0</exit>" +
                        "</ssh-response>"));
    }

    private void runAutomationTask(String host) {
        echo(String.format("Запуск задания автоматизации на хосте: %s", host));

        send(sendMessageBuilder -> sendMessageBuilder.endpoint("sshClient")
                .payload(String.format(
                        "<ssh-request xmlns=\"http://www.citrusframework.org/schema/ssh/message\">" +
                                "<command>%s</command>" +
                                "</ssh-request>",
                        COMMAND_RUN_TASK))0;

        receive(receiveMessageBuilder -> receiveMessageBuilder.endpoint("sshClient")
                .payload("<ssh-response xmlns=\"http://www.citrusframework.org/schema/ssh/message\">" +
                        "<stdout>Automation task executed successfully</stdout>" +
                        "<stderr/>" +
                        "<exit>0</exit>" +
                        "</ssh-response>"));
    }

    private void checkHostConfiguration(String host) {
        echo(String.format("Проверка конфигурации хоста: %s", host));

        send(sendMessageBuilder -> sendMessageBuilder.endpoint("sshClient")
                .payload(String.format(
                        "<ssh-request xmlns=\"http://www.citrusframework.org/schema/ssh/message\">" +
                                "<command>%s</command>" +
                                "</ssh-request>",
                        COMMAND_CHECK_CONFIGURATION)));

        receive(receiveMessageBuilder -> receiveMessageBuilder.endpoint("sshClient")
                .payload("<ssh-response xmlns=\"http://www.citrusframework.org/schema/ssh/message\">" +
                        "<stdout>" + EXPECTED_CHECK_RESPONSE + "</stdout>" +
                        "<stderr/>" +
                        "<exit>0</exit>" +
                        "</ssh-response>"));
    }
}
