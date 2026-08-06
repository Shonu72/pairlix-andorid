# Profile Details Action Bar UI Specification (Android Implementation Guide)

This document provides the exact UI design specifications, layout measurements, color codes, and logic for implementing the redesigned **Profile Details Floating Action Bar** in the Android app (XML layouts / Jetpack Compose).

---

## 🎨 1. Floating Capsule Container (`viewLikeDislike`)

The floating bar is positioned at the bottom center of the Profile Details screen over the scrollable profile content.

* **Shape**: Pill / Capsule shape (`CornerRadius = 26dp`)
* **Background Color**: `#F8F5FA` (Soft neutral light lavender/off-white, 95% opacity)
* **Border**: `1dp` solid `#FFFFFF` (80% opacity) or light `#EAEAEA`
* **Drop Shadow / Elevation**: `4dp` elevation / ambient drop shadow (`ShadowColor = #000000`, opacity `8%`, shadow radius `10dp`)
* **Height**: `72dp` (Inner content height `52dp` + `10dp` top & bottom padding)
* **Alignment**: Center horizontally at the bottom (`layout_alignParentBottom="true"` or `ConstraintLayout` bottom constraint with `margin_bottom="20dp"`).

---

## 🔘 2. Button Specifications & State Matrix

### A. Standard View (Home Feed & Activity → Received / Likes Tab)
> **Goal**: Full swiping & action choices.
* **Container Width**: `290dp`
* **Button Layout Order** *(Left to Right)*: **`[ Skip ]`** → **`[ Connect ]`** → **`[ Save ]`**

| Button | Dimensions | Background Color | Border | Icon | Text | Layout / Spacing |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **Skip** *(Left)* | Width: `58dp`<br>Height: `52dp` | `#FFFFFF`<br>(Solid White) | `1dp` solid `#E6E6E6` | Red `X` cross icon (`#F24040` / `#FF3B30`, `14dp`) | **"Skip"**<br>Font: SemiBold `11sp`<br>Color: `#333333` | **Vertical**<br>Icon top, Text bottom<br>Corner Radius: `14dp` |
| **Connect** *(Middle)* | Width: `134dp`<br>Height: `52dp` | `#7331D9`<br>(Solid Vibrant Purple) | None | White Heart outline icon (`#FFFFFF`, `16dp`) | **"Connect"**<br>Font: SemiBold `14sp`<br>Color: `#FFFFFF` | **Horizontal**<br>Icon left, Text right (`8dp` gap)<br>Corner Radius: `14dp` |
| **Save** *(Right)* | Width: `58dp`<br>Height: `52dp` | `#FFFFFF`<br>(Solid White) | `1dp` solid `#E6E6E6` | Purple Bookmark icon (`#7331D9`, `14dp`) | **"Save"**<br>Font: SemiBold `11sp`<br>Color: `#333333` | **Vertical**<br>Icon top, Text bottom<br>Corner Radius: `14dp` |

---

### B. Sent Tab (`Activity → Sent`)
> **Goal**: Read-only status indicator for sent requests + bookmarking.
* **Container Width**: `230dp`
* **Button Layout Order** *(Left to Right)*: **`[ Request Sent ]`** → **`[ Save ]`**

* **Skip Button**: `GONE` (`visibility = GONE`)
* **Request Sent Badge** *(Middle)*:
  * Width: `142dp` | Height: `52dp`
  * Background: `#8C66C7` (Muted translucent purple, ~85% opacity)
  * Icon: Filled Checkmark Circle (`ic_check_circle`, `15dp`, Color: `#F2F2F2`)
  * Text: **"Request Sent"** (Font: SemiBold `13sp`, Color: `#F2F2F2`)
  * Interaction: Disabled (`enabled = false`, read-only status badge)
* **Save Button** *(Right)*:
  * Same as Standard Save button (Width `58dp`, Bookmark icon + "Save")

---

### C. Archived Tab (`Activity → Archived`)
> **Goal**: Allow "Second Chance" / Rewind to connect with previously skipped user.
* **Container Width**: `210dp`
* **Button Layout Order** *(Left to Right)*: **`[ Connect ]`** → **`[ Save ]`**

* **Skip Button**: `GONE` (`visibility = GONE`, since profile is already archived)
* **Connect Button** *(Middle)*:
  * Width: `124dp` | Height: `52dp`
  * Same as Standard Connect button (Solid Purple `#7331D9`, White Heart + "Connect")
  * Interaction: Enabled (Tapping sends `like`/`connect` request)
* **Save Button** *(Right)*:
  * Same as Standard Save button (Width `58dp`, Bookmark icon + "Save")

---

### D. Connected / Matches Tab (`Activity → Connected`)
> **Goal**: Unmatch action only.
* **Floating Action Bar**: `GONE` (`visibility = GONE`)
* **Unmatch Button**: Display standard single **"Unmatch"** button at the bottom of the profile details content.

---

## ⚡ 3. Android Logic Summary (Activity Tab Matrix)

```kotlin
when (activityTab) {
    ActivityTab.LIKES -> {
        // Received Tab: [ Skip ]  [ Connect ]  [ Save ]
        btnSkip.visibility = View.VISIBLE
        btnConnect.visibility = View.VISIBLE
        btnSave.visibility = View.VISIBLE
        
        btnConnect.isEnabled = true
        btnConnect.text = "Connect"
        btnConnect.setBackgroundColor(Color.parseColor("#7331D9"))
        setContainerWidth(290.toDp())
    }
    ActivityTab.SENT -> {
        // Sent Tab: [ Request Sent ]  [ Save ]
        btnSkip.visibility = View.GONE
        btnConnect.visibility = View.VISIBLE
        btnSave.visibility = View.VISIBLE
        
        btnConnect.isEnabled = false
        btnConnect.text = "Request Sent"
        btnConnect.setBackgroundColor(Color.parseColor("#8C66C7"))
        setContainerWidth(230.toDp())
    }
    ActivityTab.ARCHIVED -> {
        // Archived Tab (Second Chance): [ Connect ]  [ Save ]
        btnSkip.visibility = View.GONE
        btnConnect.visibility = View.VISIBLE
        btnSave.visibility = View.VISIBLE
        
        btnConnect.isEnabled = true
        btnConnect.text = "Connect"
        btnConnect.setBackgroundColor(Color.parseColor("#7331D9"))
        setContainerWidth(210.toDp())
    }
    ActivityTab.CONNECTED -> {
        // Connected Tab: Unmatch button only
        floatingActionBarView.visibility = View.GONE
        unmatchButtonView.visibility = View.VISIBLE
    }
}
```

---

## 🎨 4. Hex Color Reference Table

| Color Name | Hex Code | Usage |
| :--- | :--- | :--- |
| **Vibrant Purple** | `#7331D9` | Connect button background, Bookmark icon tint |
| **Muted Purple** | `#8C66C7` | Request Sent badge background |
| **Skip Red** | `#F24040` | Skip `X` icon tint |
| **Container BG** | `#F8F5FA` | Floating capsule bar background |
| **Button Border** | `#E6E6E6` | Skip & Save card border |
| **Dark Text** | `#333333` | "Skip" and "Save" label text |
| **White** | `#FFFFFF` | "Connect" text & heart icon, Skip/Save background |
