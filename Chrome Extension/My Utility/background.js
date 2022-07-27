chrome.tabs.onUpdated.addListener(
  (tabId, changeInfo, tab) => {
    listen(tabId, tab)
  });

const listen = (tabId, tab) => {
    if (tab.url && tab.url.includes("meet.google.com")) {
      chrome.tabs.sendMessage(tabId, {
        utility : "g-meet"
      });
    }
  }