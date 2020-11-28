package com.smarttech;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class PremierLeague {

	public static void main(String[] args) throws Throwable {
		PremierLeague.findMissingTeams();

	}

	static void findMissingTeams() throws Throwable {
		ArrayList<String> oldTeams = new ArrayList<String>();
		ArrayList<String> newTeams = new ArrayList<String>();
		FileInputStream fis = new FileInputStream(new File("./excelJahid/ApacheReader.xls"));
		HSSFWorkbook wb = new HSSFWorkbook(fis);
		HSSFSheet sheet = wb.getSheetAt(0);
		for (int i = 0; i < sheet.getLastRowNum(); i++) {

			oldTeams.add(sheet.getRow(i).getCell(0).getStringCellValue());

		}
		System.out.println("List of teams in old list" + oldTeams);

		System.setProperty("webdriver.chrome.driver", "c:/Driver/chromedriver.exe");
		// instantiate a Chrome session
		WebDriver driver = new ChromeDriver();
		// Open browser and go to home page
		driver.get("https://www.premierleague.com/tables");
		// declare implicity wait
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		// close popup
		driver.findElement(By.xpath("//*[contains(text(),'I accept cookies from this site')]")).click();

		List<WebElement> teams = driver.findElements(By.xpath("//table/tbody/tr/td[3]/a/span[2]"));

		for (int i = 0; i < teams.size(); i++) {
			newTeams.add(teams.get(i).getText().toString());
		}

		System.out.println("List of teams in new list" + newTeams);

		newTeams.removeAll(oldTeams);

		System.out.println("These teams are missing: " + newTeams);
	}

	static void captureAllTeamNames() throws Throwable {
		System.setProperty("webdriver.chrome.driver", "c:/Driver/chromedriver.exe");
		// instantiate a Chrome session
		WebDriver driver = new ChromeDriver();
		// Open browser and go to home page
		driver.get("https://www.premierleague.com/tables");
		// declare implicity wait
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		// close popup
		driver.findElement(By.xpath("//*[contains(text(),'I accept cookies from this site')]")).click();

		List<WebElement> teams = driver.findElements(By.xpath("//table/tbody/tr/td[3]/a/span[2]"));

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		for (int i = 0; i < teams.size(); i++) {
			Row row = sheet.createRow(i);
			Cell cell = row.createCell(0);
			cell.setCellValue(teams.get(i).getText().trim());
			FileOutputStream fso = new FileOutputStream("./excelJahid/ApacheReader.xls");
			wb.write(fso);
			fso.close();

		}
		System.out.println("Successfully created excel file");

	}

}
