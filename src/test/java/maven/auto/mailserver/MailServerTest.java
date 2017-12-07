package maven.auto.mailserver;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MailServerTest {

	private static WebDriver driver;
	public static WebDriverWait wait;

	@BeforeClass
	public static void setup() {
		System.setProperty("webdriver.chrome.driver", "d:/Soft/Selenium/chromedriver/chromedriver.exe");
		driver = new ChromeDriver();
		driver.get("http://mail.ru");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		wait = new WebDriverWait(driver, 10);
	}

	@Test
	public void mailServerTest() {
		userLogin();
		Assert.assertEquals(true, selectLetters());
		logOut();
	}

	private boolean selectLetters() {
		WebElement outboxElement = wait.until(ExpectedConditions.elementToBeClickable(By
				.cssSelector("div#b-nav_folders a[href='/messages/sent/']")));
		outboxElement.click();

		WebElement inboxElement = wait.until(ExpectedConditions.elementToBeClickable(By
				.cssSelector("div#b-nav_folders a[href='/messages/inbox/']")));
		inboxElement.click();

		List<WebElement> letters = driver.findElements(By
				.cssSelector("div#b-letters a[data-subject='Re: new test css']"));

		boolean isTestPassed = false;

		for (Iterator<WebElement> iter = letters.iterator(); iter.hasNext();) {
			WebElement link = iter.next();
			isTestPassed = isTestPassed || checkLetter(link);
		}

		return isTestPassed;
	}

	private void userLogin() {
		WebElement loginField = driver.findElement(By.id("mailbox:login"));
		loginField.sendKeys("alex.test.1990");

		WebElement domainField = driver.findElement(By.id("mailbox:domain"));
		Select selectDomain = new Select(domainField);
		selectDomain.selectByVisibleText("@inbox.ru");

		WebElement passField = driver.findElement(By.id("mailbox:password"));
		passField.sendKeys("csservice123");

		WebElement loginButton = driver.findElement(By.id("mailbox:submit"));
		loginButton.click();

	}

	private boolean checkLetter(final WebElement link) {
		link.click();

		List<WebElement> sender = driver.findElements(By
				.cssSelector("div#b-letter span[data-contact-informer-email='alexander.mosin100@gmail.com']"));

		List<WebElement> mailBody = driver.findElements(By.xpath("//div[text()='letter 2']"));

		if (sender.size() == 1 && mailBody.size() == 1)
			return true;
		else
			return false;

		// WebElement sender = wait.until(ExpectedConditions.elementToBeClickable(By
		// .cssSelector("div#b-letter span[data-contact-informer-email='alexander.mosin100@gmail.com']")));
		//
		// WebElement mailBody = wait.until(ExpectedConditions.presenceOfElementLocated(By
		// .xpath("//div[text()='letter 2']")));

	}

	private void logOut() {
		WebElement logoutLink = driver.findElement(By.id("PH_logoutLink"));
		logoutLink.click();
		driver.quit();
	}

}
