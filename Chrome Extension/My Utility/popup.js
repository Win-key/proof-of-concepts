import { getCurrentTab } from "./utils.js";

document.addEventListener("DOMContentLoaded", async () => {
    const activeTab = await getCurrentTab();

    const container = document.querySelector('#utility-container');

    const utility = document.createElement("div")
    const utilityTitle = document.createElement("div")

    if(activeTab.url.includes("meet.google.com")) {
        utilityTitle.textContent = "G Meet utility"
    } else {
        utilityTitle.textContent = "No utitility found"
    }

    utilityTitle.className = "bookmark-title"

    utility.className = "bookmakr"
    utility.appendChild(utilityTitle)
    container.appendChild(utility)

});
