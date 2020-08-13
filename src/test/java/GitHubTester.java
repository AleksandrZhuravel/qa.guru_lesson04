import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.*;

@Owner("zhuravel")
@Feature("Работа с задачами")
public class GitHubTester {
    private String baseUrl = "https://github.com";
    private String repository = "testZhuravel/qa.guru_lesson04";
    private String issueName = "QAGURULessonFour";
    private String userLogin = "testZhuravel";
    private String userPass = "Xx00000_";
    private String tagName = "    None yet";

    private Steps steps = new Steps();

    @BeforeAll
    public void initLogger() {
        SelenideLogger.addListener("allure", new AllureSelenide()
                .savePageSource(true)
                .screenshots(true));
    }

    @AfterEach
    public void closeDriver() {
        closeWebDriver();
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    // 1. Чистый Selenide + Чистый Rest-Assured c листенерами
    @Test
    @Story("Проверка корректности создания новой задачи")
    @DisplayName("01. Пользователь должен иметь возможность найти Issue по названию, исполнителю, тегам")
    public void shouldFindIssueByNameTagExecutorFirst() {
        link("GitHub", String.format("%s/%s", baseUrl, repository));
        parameter("Репозиторий", repository);

        open(baseUrl);

        $(by("href", "/login")).click();
        $(by("id", "login_field")).val(userLogin);
        $(by("id", "password")).val(userPass);
        $(".btn-primary").click();
        $(".header-search-input").click();
        $(".header-search-input").sendKeys(repository);
        $(".header-search-input").submit();
        $(By.linkText(repository)).click();
        $(by("data-content", "Issues")).click();
        $x("//*[@id=\"js-repo-pjax-container\"]/div[3]/div/div/div[2]/div[2]/a").click();
        $(by("id", "issue_title")).val(issueName);
        $(byText("Submit new issue")).click();

        $(".js-issue-title").shouldHave(text(issueName));
        $x("//*[@id=\"partial-discussion-header\"]/div[3]/div[2]/a").shouldHave(text(userLogin));
        $(".js-issue-labels").shouldHave(text(tagName));
    }

    // 2. Лямбда шаги через step (name, () -> {})
    @Test
    @Story("Проверка корректности создания новой задачи")
    @DisplayName("02. Пользователь должен иметь возможность найти Issue по названию, исполнителю, тегам")
    public void shouldFindIssueByNameTagExecutorSecond() {
        link("GitHub", String.format("%s/%s", baseUrl, repository));
        parameter("Репозиторий", repository);

        step("Открыть главную страницу", () -> {
            open(baseUrl);
        });

        step("Нажать кнопку 'Sign in'", () -> {
            $(by("href", "/login")).click();
        });
        step("Авторизовать Пользователя", () -> {
            $(by("id", "login_field")).val(userLogin);
            $(by("id", "password")).val(userPass);
            $(".btn-primary").click();
        });

        step("Найти репозиторий " + repository, () -> {
            $(".header-search-input").click();
            $(".header-search-input").sendKeys(repository);
            $(".header-search-input").submit();
        });
        step("Перейти по ссылке репозитория " + repository, () -> {
            $(By.linkText(repository)).click();
        });
        step("Открыть страницу с задачами", () -> {
            $(by("data-content", "Issues")).click();
        });
        step("Нажать кнопку 'New issue'", () -> {
            $x("//*[@id=\"js-repo-pjax-container\"]/div[3]/div/div/div[2]/div[2]/a").click();
        });
        step("Создать новое Issue" + issueName, () -> {
            $(by("id", "issue_title")).val(issueName);
            $(byText("Submit new issue")).click();
        });

        step("Проверить правильность указания названия Issue", () -> {
            $(".js-issue-title").shouldHave(text(issueName));
        });
        step("Проверить правильность указания логина исполнителя", () -> {
            $x("//*[@id=\"partial-discussion-header\"]/div[3]/div[2]/a").shouldHave(text(userLogin));
        });
        step("Проверить правильность указания тега", () -> {
            $(".js-issue-labels").shouldHave(text(tagName));
        });
    }

    // 3. Шаги с аннотацией @Step
    @Test
    @Story("Проверка корректности создания новой задачи")
    @DisplayName("03. Пользователь должен иметь возможность найти Issue по названию, исполнителю, тегам")
    public void shouldFindIssueByNameTagExecutorThird() {
        link("GitHub", String.format("%s/%s", baseUrl, repository));
        parameter("Репозиторий", repository);
        steps.openMainPage();
        steps.pressSignButton();
        steps.authorizeUser(userLogin, userPass);
        steps.findRepo(repository);
        steps.followRepositoryLink(repository);
        steps.openRepoPage();
        steps.pressNewIssueButton();
        steps.createNewIssue(issueName);
        steps.checkIssueName(issueName);
        steps.checkUserLogin(userLogin);
        steps.checkTag(tagName);
    }
}
