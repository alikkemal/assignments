package ui;

import base.BaseUITest;
import models.pages.CareersPage;
import models.pages.HomePage;
import models.pages.LeverApplicationPage;
import models.visitor.user.User;
import models.visitor.user.UserPool;
import org.junit.Test;

import static models.visitor.user.User.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class HomePageTest extends BaseUITest {

    @Test
    public void shouldSeeJobFormAsFilteredJobsToFollowingHomePageComponent() {
        HomePage homePage = new HomePage();
        User user = UserPool.anonymous().open(homePage, "firefox");

        assertThat("When user tried to open home page", user, shouldBeRedirectedTo(homePage));

        homePage = ((HomePage) user.nowLookingAt()).acceptCookies();
        homePage.selectNavigationBarItem("More").selectNavigationBarSubItem("Careers");

        CareersPage careersPage = new CareersPage();
        user.changePage(careersPage);

        assertThat("When user tried to open careers page", user, shouldBeRedirectedTo(careersPage));

        careersPage.clickSeeAllTeamsButton();
        assertThat("When user tried to see block", user, shouldSeeTheTeamsJobFields(careersPage.getAllTeamsField()));
        assertThat("When user tried to see block", user, shouldSeeTheLocationsFields(careersPage.getAllLocationsField()));
        assertThat("When user tried to see block", user, shouldSeeTheLifeInsiderBlocks(13));

        careersPage.selectTeamsItem("Quality Assurance")
                .seeAllQAJobs()
                .filterLocation("Istanbul, Turkey")
                .filterDepartment("Quality Assurance");

        assertThat("When user tried to see block", user, shouldSeeTheTeamsJobFieldsAs("Quality Assurance", "Istanbul, Turkey"));
        assertThat("When user tried to see block", user, shouldSeeTheButton("Apply Now"));

        String url = careersPage.selectDefaultJobs();
        user.browser().navigateUrl(url);
        LeverApplicationPage leverApplicationPage = new LeverApplicationPage();

        assertThat("When user tried to open careers page", user, shouldBeRedirectedTo(leverApplicationPage));
    }
}
