console.log('#### contentScript.js')


let started = false;
setInterval(() => {
  const target = document.querySelector('.reflex-element');
  const observer = new MutationObserver((mutations) => {
    console.log('callback..')
    mutations.forEach((mutation) => {
    });

    //const talkMessageArea = document.querySelector('#talk-message-area');
    const talkMessageArea = $('#talk-message-area');
    console.log('talkMessageArea.length : ' + talkMessageArea.length)
    if (talkMessageArea.length) {
      talkMessageArea.append('<div>copy</div>');
    }
  });

  if (target) {
    if (!started) {
      const config = {
        //attributes: true, // 속성 변화 할때 감지
        childList: true, // 자식노드 추가/제거 감지
        subtree: true,
        //characterData: true // 데이터 변경전 내용 기록
      };

      observer.observe(target, config);
      console.log('감지 시작...')
      started = true;
    } else {
    }
  } else {
    console.log('target is null');
    observer.disconnect();
    started = false;
  }
}, 1000)

/*


const target = document.querySelector('.reflex-element');
const config = {
  attributes: true, // 속성 변화 할때 감지
  childList: true, // 자식노드 추가/제거 감지
  subtree: true,
  characterData: true // 데이터 변경전 내용 기록
};
const observer = new MutationObserver((mutations) => {
  console.log('callback..')
  mutations.forEach((mutation) => {
    // mutation: 감지된 내용
    console.log('감지된 내용...')
  })
});
observer.observe(target, config);
*/







// Communicate with background file by sending a message
chrome.runtime.sendMessage(
  {
    type: 'GREETINGS',
    payload: {
      message: 'Hello, my name is Con. I am from ContentScript.',
    },
  },
  (response) => {
    console.log(response.message);
  }
);

// Listen for message
chrome.runtime.onMessage.addListener((request, sender, sendResponse) => {
  if (request.type === 'COUNT') {
    console.log(`Current count is ${request.payload.count}`);
  }

  // Send an empty response
  // See https://github.com/mozilla/webextension-polyfill/issues/130#issuecomment-531531890
  sendResponse({});
  return true;
});
