package com.tqa.adl.sampletest;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;

public class AdlAutomationScriptTests {

	@BeforeAll
	public static void setUp() {
		// Настройка Selenide для ра`боты в headless режиме
		Configuration.headless = true;
		Configuration.browser = "chrome"; // Используем Chrome в headless-режиме
	}

	@AfterAll
	public static void tearDown() {
		// Закрытие браузера после выполнения теста
		Selenide.closeWebDriver();
	}

	@BeforeAll
	public void beforeEach() {
		// Переход на главную страницу приложения перед каждым тестом
		Selenide.open("http://some-app-url"); // Замените на реальный URL
	}

	@Test
	public void testStartAutomationTask() {
		// Шаг 1: Переход по меню: Автоматизация -> Задания автоматизации
		$(byText("Автоматизация")).click();
		$(byText("Задания автоматизации")).click();

		// Шаг 2: Переключение на вкладку "Каталог заданий автоматизации"
		$(byText("Каталог заданий автоматизации")).click();

		// Шаг 3: Открытие карточки задания
		$(byText("test")).click();

		// Шаг 4: Нажатие кнопки "Запустить задание"
		$(byText("Запустить задание")).click();

		// Ожидаемый результат: Отображается форма запуска задания
		$("form#start-task").should(appear());
	}

	@Test
	public void testSelectComputersGroupsAndDepartments() {
		// Шаг 1: Перенос компьютера host в таблицу "Выбранные компьютеры"
		$("#computer-table").$(byText("host")).dragAndDropTo($("#selected-computers"));

		// Шаг 2: Перенос группы group в таблицу "Выбранные группы"
		$("#group-table").$(byText("group")).dragAndDropTo($("#selected-groups"));

		// Шаг 3: Перенос подразделения A в таблицу "Выбранные подразделения"
		$("#department-table").$(byText("A")).dragAndDropTo($("#selected-departments"));

		// Шаг 4: Нажатие кнопки "Сохранить"
		$(byText("Сохранить")).click();

		// Подтверждение операции
		$(byText("Подтвердить")).click();

		// Ожидаемый результат: Запрос на запуск задания выполнен
		$("#task-status").shouldHave(text("Задание запущено"));
	}

	@Test
	public void testTaskJournal() {
		// Шаг 1: Переход по меню: Автоматизация -> Задания автоматизации -> Журнал
		// заданий
		$(byText("Автоматизация")).click();
		$(byText("Задания автоматизации")).click();
		$(byText("Журнал заданий")).click();

		// Ожидаемый результат: В журнале отображен запуск задания с правильным статусом
		// и инициатором
		$("#task-journal").shouldHave(text("Статус: Отправлено"));
		$("#task-journal").shouldHave(text("Инициатор: Иванов Иван Иванович"));
	}
}
