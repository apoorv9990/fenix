/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

@file:Suppress("TooManyFunctions")

package org.mozilla.fenix.ui.robots

import android.net.Uri
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withChild
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By.res
import androidx.test.uiautomator.By.text
import androidx.test.uiautomator.UiDevice
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.containsString
import org.junit.Assert.assertEquals
import org.mozilla.fenix.R
import org.mozilla.fenix.helpers.TestAssetHelper
import org.mozilla.fenix.helpers.click
import org.mozilla.fenix.helpers.ext.waitNotNull

/**
 * Implementation of Robot Pattern for the bookmarks menu.
 */
class BookmarksRobot {

    fun verifyBookmarksMenuView() = assertBookmarksView()

    fun verifyEmptyBookmarksList() = assertEmptyBookmarksList()

    fun verifyBookmarkFavicon() = assertBookmarkFavicon()

    fun verifyBookmarkedURL(url: Uri) = assertBookmarkURL(url)

    fun verifyFolderTitle(title: String) {
        mDevice.waitNotNull(
            Until.findObject(text(title)),
            TestAssetHelper.waitingTime
        )
        assertFolderTitle(title)
    }

    fun verifyBookmarkTitle(title: String) {
        mDevice.waitNotNull(
            Until.findObject(text(title)),
            TestAssetHelper.waitingTime
        )
        assertBookmarkTitle(title)
    }

    fun verifyDeleteSnackBarText() = assertDeleteSnackBarText()

    fun verifyUndoDeleteSnackBarButton() = assertUndoDeleteSnackBarButton()

    fun verifySnackBarHidden() {
        mDevice.waitNotNull(
            Until.gone(By.text("UNDO")),
            TestAssetHelper.waitingTime
        )
        onView(withId(R.id.snackbar_layout)).check(doesNotExist())
    }

    fun verifyCopySnackBarText() = assertCopySnackBarText()

    fun verifyEditBookmarksView() = assertEditBookmarksView()

    fun verifyBookmarkNameEditBox() = assertBookmarkNameEditBox()

    fun verifyBookmarkURLEditBox() = assertBookmarkURLEditBox()

    fun verifyParentFolderSelector() = assertBookmarkFolderSelector()

    fun verifyKeyboardHidden() = assertKeyboardVisibility(isExpectedToBeVisible = false)

    fun verifyKeyboardVisible() = assertKeyboardVisibility(isExpectedToBeVisible = true)

    fun verifyShareOverlay() = assertShareOverlay()

    fun verifyShareBookmarkFavicon() = assertShareBookmarkFavicon()

    fun verifyShareBookmarkTitle() = assertShareBookmarkTitle()

    fun verifyShareBookmarkUrl() = assertShareBookmarkUrl()

    fun verifyCurrentFolderTitle(title: String) {
        onView(
            allOf(
                withText(title),
                withParent(withId(R.id.navigationToolbar))
            )
        )
            .check(matches(isDisplayed()))
    }

    fun createFolder(name: String) {
        clickAddFolderButton()
        addNewFolderName(name)
        saveNewFolder()
    }

    fun clickAddFolderButton() {
        mDevice.waitNotNull(
            Until.findObject(By.desc("Add folder")),
            TestAssetHelper.waitingTime
        )
        addFolderButton().click()
    }

    fun addNewFolderName(name: String) {
        addFolderTitleField()
            .click()
            .perform(clearText())
            .perform(typeText(name))
    }

    fun saveNewFolder() {
        saveFolderButton().click()
    }

    fun navigateUp() {
        goBackButton().click()
    }

    fun clickUndoDeleteButton() {
        snackBarUndoButton().click()
    }

    fun changeBookmarkTitle(newTitle: String) {
        bookmarkNameEditBox()
            .perform(clearText())
            .perform(typeText(newTitle))
    }

    fun changeBookmarkUrl(newUrl: String) {
        bookmarkURLEditBox()
            .perform(clearText())
            .perform(typeText(newUrl))
    }

    fun saveEditBookmark() {
        saveBookmarkButton().click()
        mDevice.waitNotNull(Until.findObject(text("Bookmarks")))
    }

    fun clickParentFolderSelector() = bookmarkFolderSelector().click()

    fun selectFolder(title: String) = onView(withText(title)).click()

    class Transition {
        fun goBack(interact: HomeScreenRobot.() -> Unit): HomeScreenRobot.Transition {
            goBackButton().click()

            HomeScreenRobot().interact()
            return HomeScreenRobot.Transition()
        }

        fun openThreeDotMenu(interact: ThreeDotMenuBookmarksRobot.() -> Unit): ThreeDotMenuBookmarksRobot.Transition {
            mDevice.waitNotNull(Until.findObject(res("org.mozilla.fenix.debug:id/overflow_menu")))
            threeDotMenu().click()

            ThreeDotMenuBookmarksRobot().interact()
            return ThreeDotMenuBookmarksRobot.Transition()
        }

        fun openThreeDotMenu(bookmarkTitle: String, interact: ThreeDotMenuBookmarksRobot.() -> Unit): ThreeDotMenuBookmarksRobot.Transition {
            mDevice.waitNotNull(Until.findObject(res("org.mozilla.fenix.debug:id/overflow_menu")))
            threeDotMenu(bookmarkTitle).click()

            ThreeDotMenuBookmarksRobot().interact()
            return ThreeDotMenuBookmarksRobot.Transition()
        }
    }
}

fun bookmarksMenu(interact: BookmarksRobot.() -> Unit): BookmarksRobot.Transition {
    BookmarksRobot().interact()
    return BookmarksRobot.Transition()
}

private fun goBackButton() = onView(withContentDescription("Navigate up"))

private fun bookmarkFavicon() = onView(withId(R.id.favicon))

private fun bookmarkURL() = onView(withId(R.id.url))

private fun bookmarkTitle() = onView(withId(R.id.title))

private fun folderTitle() = onView(withId(R.id.title))

private fun addFolderButton() = onView(withId(R.id.add_bookmark_folder))

private fun addFolderTitleField() = onView(withId(R.id.bookmarkNameEdit))

private fun saveFolderButton() = onView(withId(R.id.confirm_add_folder_button))

private fun threeDotMenu(folderName: String) = onView(
    allOf(
        withId(R.id.overflow_menu),
        withParent(withChild(allOf(withId(R.id.title), withText(folderName))))
    )
)

private fun threeDotMenu() = onView(withId(R.id.overflow_menu))

private fun snackBarText() = onView(withId(R.id.snackbar_text))

private fun snackBarUndoButton() = onView(withId(R.id.snackbar_btn))

private fun bookmarkNameEditBox() = onView(withId(R.id.bookmarkNameEdit))

private fun bookmarkFolderSelector() = onView(withId(R.id.bookmarkParentFolderSelector))

private fun bookmarkURLEditBox() = onView(withId(R.id.bookmarkUrlEdit))

private fun saveBookmarkButton() = onView(withId(R.id.save_bookmark_button))

private fun assertBookmarksView() {
    onView(
        allOf(
            withText("Bookmarks"),
            withParent(withId(R.id.navigationToolbar))
        )
    )
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
}

private fun assertEmptyBookmarksList() =
    onView(withId(R.id.bookmarks_empty_view)).check(matches(withText("No bookmarks here")))

private fun assertBookmarkFavicon() = bookmarkFavicon().check(
    matches(
        withEffectiveVisibility(
            ViewMatchers.Visibility.VISIBLE
        )
    )
)

private fun assertBookmarkURL(expectedURL: Uri) = bookmarkURL()
    .check(matches(ViewMatchers.isCompletelyDisplayed()))
    .check(matches(withText(containsString(expectedURL.toString()))))

private fun assertFolderTitle(expectedTitle: String) = folderTitle()
    .check(matches(withText(expectedTitle)))
    .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

private fun assertBookmarkTitle(expectedTitle: String) = bookmarkTitle()
    .check(matches(withText(expectedTitle)))
    .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

private fun assertDeleteSnackBarText() =
    snackBarText().check(matches(withText(containsString("Deleted"))))

private fun assertUndoDeleteSnackBarButton() =
    snackBarUndoButton().check(matches(withText("UNDO")))

private fun assertCopySnackBarText() = snackBarText().check(matches(withText("URL copied")))

private fun assertEditBookmarksView() = onView(withText("Edit bookmark"))
    .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

private fun assertBookmarkNameEditBox() =
    onView(withId(R.id.bookmarkNameEdit))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

private fun assertBookmarkFolderSelector() =
    onView(withId(R.id.bookmarkParentFolderSelector))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

private fun assertBookmarkURLEditBox() =
    onView(withId(R.id.bookmarkUrlEdit))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

private fun assertKeyboardVisibility(isExpectedToBeVisible: Boolean) =
    assertEquals(
        isExpectedToBeVisible,
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            .executeShellCommand("dumpsys input_method | grep mInputShown")
            .contains("mInputShown=true")
    )

private fun assertShareOverlay() =
    onView(withId(R.id.shareWrapper)).check(matches(ViewMatchers.isDisplayed()))

private fun assertShareBookmarkTitle() =
    onView(withId(R.id.share_tab_title)).check(matches(ViewMatchers.isDisplayed()))

private fun assertShareBookmarkFavicon() =
    onView(withId(R.id.share_tab_favicon)).check(matches(ViewMatchers.isDisplayed()))

private fun assertShareBookmarkUrl() =
    onView(withId(R.id.share_tab_url)).check(matches(isDisplayed()))
