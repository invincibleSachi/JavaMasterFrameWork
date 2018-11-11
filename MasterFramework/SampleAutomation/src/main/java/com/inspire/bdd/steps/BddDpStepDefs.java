package com.inspire.bdd.steps;

import org.openqa.selenium.WebDriver;

import com.inspire.abstest.AbstractTest;
import com.inspire.app.pages.MainPage;
import com.inspire.app.pages.SampleLoginPage;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class BddDpStepDefs extends AbstractTest {
	WebDriver driver = null;
	SampleLoginPage loginPage = null;

	@Given("^I am loged into data platform$")
	public MainPage i_am_loged_into_data_platform() throws Throwable {
		driver = getDriver();
		loginPage = new SampleLoginPage(driver);
		loginPage.getUrl(environment.get("baseurl"));
		MainPage mainPage = loginPage.login(environment.get("admin"), environment.get("adminpwd"));
		return mainPage;

	}

	@Given("^I want to write a step with name(\\d+)$")
	public void i_want_to_write_a_step_with_name(int arg1) throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		throw new PendingException();
	}

	@When("^I check for the (\\d+) in step$")
	public void i_check_for_the_in_step(int arg1) throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		throw new PendingException();
	}

	@Then("^I verify the success in step$")
	public void i_verify_the_success_in_step() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		throw new PendingException();
	}

	@Then("^I verify the Fail in step$")
	public void i_verify_the_Fail_in_step() throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		throw new PendingException();
	}
}
