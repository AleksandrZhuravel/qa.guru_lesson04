import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

public class Steps {
    private String baseUrl = "https://github.com";


    @Step("Открыть главную страницу")
    public void openMainPage() {
        open(baseUrl);
    }

    @Step("Нажать кнопку 'Sign in")
    public void pressSignButton() {
        $(by("href", "/login")).click();
    }

    @Step("Авторизовать Пользователя")
    public void authorizeUser(String userLogin, String userPass) {
        $("#login_field").val(userLogin);
        $("#password").val(userPass);
        $(".btn-primary").click();
    }

    @Step("Найти репозиторий")
    public void findRepo(String repository) {
        $(".header-search-input").click();
        $(".header-search-input").sendKeys(repository);
        $(".header-search-input").submit();
    }

    @Step("Перейти по ссылке репозитория")
    public void followRepositoryLink(String repository) {
        $(By.linkText(repository)).click();
    }

    @Step("Открыть страницу с задачами")
    public void openRepoPage() {
        $(by("data-content", "Issues")).click();
    }

    @Step("Нажать кнопку 'New issue'")
    public void pressNewIssueButton() {
        $(".btn-primary", 2).click();
    }

    @Step("Создать новое Issue")
    public void createNewIssue(String issueName) {
        $("#issue_title").val(issueName);
        $(byText("Submit new issue")).click();
    }

    @Step("Проверить правильность указания названия Issue")
    public void checkIssueName(String issueName) {
        $(".js-issue-title").shouldHave(text(issueName));
    }

    @Step("Проверить правильность указания логина исполнителя")
    public void checkUserLogin(String userLogin) {
        $(withText("opened this issue")).shouldHave(text(userLogin));
    }

    @Step("Проверить правильность указания тега")
    public void checkTag(String tagName) {
        $(".js-issue-labels").shouldHave(text(tagName));
    }

}
