# Saved Profiles (Bookmark) Feature Specification for iOS Implementation

This specification details the design, data architecture, local storage manager, and UI integration for implementing the **Saved Profiles (Bookmark)** feature in the iOS app (`Pairlix-user-ios-M6`) using Swift / UIKit.

---

## 🎯 Overview & Objective

The **Saved Profiles** feature allows users to bookmark profiles from either the **Home Discover feed** or the **Profile Details screen**. 

Since there is currently no backend API endpoint for bookmarks, profiles are persisted **locally on the device** via `UserDefaults` (using JSON encoding with `Codable`). Saved profiles are displayed in a dedicated **"Saved"** tab inside the **Activity Navigation**.

---

## 💾 1. Local Storage Architecture (`SavedProfilesManager.swift`)

Create a singleton helper `SavedProfilesManager.swift` to handle reading, writing, and checking saved profiles.

### Data Model Requirement
Ensure your profile model (e.g. `MatchProfileData` or `GetMatchResponse.Data`) conforms to `Codable` and `Equatable` (or has a unique `userId: String` property).

```swift
import Foundation

class SavedProfilesManager {
    static let shared = SavedProfilesManager()
    private let prefKey = "saved_profiles_list"
    private init() {}

    /// Retrieve list of saved profiles from UserDefaults
    func getSavedProfiles() -> [MatchProfileData] {
        guard let data = UserDefaults.standard.data(forKey: prefKey) else { return [] }
        do {
            let profiles = try JSONDecoder().decode([MatchProfileData].self, from: data)
            return profiles
        } catch {
            print("Error decoding saved profiles: \(error)")
            return []
        }
    }

    /// Save a profile to UserDefaults if not already saved
    @discardableResult
    func saveProfile(_ profile: MatchProfileData) -> Bool {
        var currentList = getSavedProfiles()
        let userId = profile.userId ?? profile.id ?? ""
        
        // Prevent duplicate saves
        if !currentList.contains(where: { ($0.userId ?? $0.id) == userId }) {
            currentList.insert(profile, at: 0) // Newest first
            do {
                let encodedData = try JSONEncoder().encode(currentList)
                UserDefaults.standard.set(encodedData, forKey: prefKey)
                return true
            } catch {
                print("Error encoding saved profile: \(error)")
            }
        }
        return false
    }

    /// Remove a profile by userId
    func removeProfile(userId: String) {
        var currentList = getSavedProfiles()
        currentList.removeAll { ($0.userId ?? $0.id) == userId }
        do {
            let encodedData = try JSONEncoder().encode(currentList)
            UserDefaults.standard.set(encodedData, forKey: prefKey)
        } catch {
            print("Error encoding saved profiles: \(error)")
        }
    }

    /// Check if a profile is already saved
    func isProfileSaved(userId: String) -> Bool {
        guard !userId.isEmpty else { return false }
        return getSavedProfiles().contains(where: { ($0.userId ?? $0.id) == userId })
    }
}
```

---

## 🔘 2. Save Trigger Points & Dynamic Unsave Logic (UI Integration)

### A. Home Feed Discover Card (`MatchProfileCardCell.swift` / `HomeVc.swift`)
- **Action**: When tapping the top-right bookmark button on a profile card:
  1. Call `SavedProfilesManager.shared.saveProfile(matchData)`.
  2. Show a toast message: `"Saved to bookmarks"`.

### B. Profile Details Screen (`MatchProfileDetailVc.swift`)
- **Dynamic Toggle State**: Check `SavedProfilesManager.shared.isProfileSaved(userId: profile.userId)`.
  - **If Already Saved**: Display a filled bookmark icon with **"Unsave"** label (red tint `#F24040`). Tapping it calls `SavedProfilesManager.shared.removeProfile(userId: ...)` and shows toast `"Removed from bookmarks"`.
  - **If Not Saved**: Display an outline bookmark icon with **"Save"** label (purple tint `#7331D9`). Tapping it calls `SavedProfilesManager.shared.saveProfile(...)` and shows toast `"Saved to bookmarks"`.
- **Saved Tab View Mode (`showBottomActions = 4`)**:
  - The `Skip` button is **hidden**.
  - Container width is `210.dp` / `210pt`.
  - Displays `[ Connect ]` (`124dp`) and `[ Unsave ]` (`58dp`).

---

## 📱 3. Activity Screen Integration (`ActivityVc.swift`)

### A. Activity Segment / Chip Bar
Update the Activity tab chips to include **"Saved"** as the 5th item:

| Index | Segment / Chip Title | Language (English) | Language (Arabic) |
| :---: | :--- | :--- | :--- |
| `0` | Received Likes | Likes | الإعجابات |
| `1` | Sent Likes | Like Sent | تم إرسال إعجاب |
| `2` | Connected Matches | Matches | المطابقات |
| `3` | Archived / Skipped | Archived | المستبعدة |
| **`4`** | **Saved Profiles** | **Saved** | **المحفوظة** |

```swift
let activityChips = ["Likes", "Like Sent", "Matches", "Archived", "Saved"]
```

### B. Data Source & Grid Rendering
- When `selectedChipIndex == 4` (Saved):
  - Set grid data source to `SavedProfilesManager.shared.getSavedProfiles()`.
  - Reload collection view / table view.

### C. Empty State
- If `selectedChipIndex == 4` and `savedProfiles.isEmpty`:
  - Show empty state icon (`nodata_image`).
  - Text: `"No saved profiles yet. Profiles you save will appear here."` (Arabic: `"لا توجد ملفات شخصية محفوظة بعد. ستظهر الملفات التي تحفظها هنا."`).

### D. Tapping a Saved Card
- Tapping a profile card in the **Saved** tab navigates to `MatchProfileDetailVc.swift`.
- Passes the selected profile and sets `showBottomActions = 4` (Saved profile view mode: `[ Connect ]` & `[ Unsave ]`).

---

## 🎨 4. Summary Checklist for iOS Developer / Gemini Prompt

1. [ ] Implement `SavedProfilesManager.swift` using `UserDefaults` and `Codable`.
2. [ ] Update `MatchProfileCardCell.swift` bookmark tap handler to call `saveProfile(...)`.
3. [ ] Update `MatchProfileDetailVc.swift` Save button to dynamically toggle between **Save** and **Unsave**.
4. [ ] In `MatchProfileDetailVc.swift`, handle `showBottomActions = 4` to hide the `Skip` button when opened from the Saved tab.
5. [ ] Add `"Saved"` (`"المحفوظة"`) to `activityChips` array in `ActivityVc.swift`.
6. [ ] Handle `selectedChipIndex == 4` in `ActivityVc.swift` to render saved profiles from `SavedProfilesManager`.
7. [ ] Implement empty state view for the Saved tab.
