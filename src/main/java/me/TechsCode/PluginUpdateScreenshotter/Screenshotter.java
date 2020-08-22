package me.TechsCode.PluginUpdateScreenshotter;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.Point;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class Screenshotter {

    public Screenshotter() {
        WebDriverManager.chromedriver().setup();
    }

    public BufferedImage createScreen(String plugin) {
        WebDriver browser = new ChromeDriver();

        browser.get("https://"+plugin+".com/updates?copyMode");
        browser.manage().window().maximize();

        Optional<WebElement> update = browser.findElements(By.className("update")).stream().findFirst();

        if(!update.isPresent()){
            browser.close();

            return null;
        }

        try {
            File screen = ((TakesScreenshot) browser).getScreenshotAs(OutputType.FILE);

            BufferedImage fullImg = ImageIO.read(screen);
            Image tmp = fullImg.getScaledInstance(fullImg.getWidth() / 2, fullImg.getHeight() / 2, Image.SCALE_SMOOTH);
            BufferedImage resized = new BufferedImage(fullImg.getWidth() / 2, fullImg.getHeight() / 2, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = resized.createGraphics();
            g2d.drawImage(tmp, 0, 0, null);
            g2d.dispose();

            WebElement element = update.get();

            // Get width and height of the element
            int w = element.getSize().getWidth();
            int h = element.getSize().getHeight();
            int x = element.getLocation().x;
            int y = element.getLocation().y;

            // Crop the entire page screenshot to get only element screenshot
            BufferedImage bufferedImage = resized.getSubimage(x,y, w, h);

            // Close Browser
            browser.close();

            return bufferedImage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
