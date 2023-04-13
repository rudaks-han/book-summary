import { v4 as uuidv4 } from "uuid";
import { fetchSSE } from "./lib/fetch-sse.js";

let KEY_ACCESS_TOKEN = '';

export default class ChatGptClient {

	async init() {
		const accessToken = await this.getAccessToken();
		console.log('accessToken: ' + accessToken);

		let conversationId = await this.getConversationId();
		console.log('conversationId: ' + conversationId)
		//this.getSummary('나는 학교가 버스로 가고 있습니다.')

    const question = '(필수) 지점 사용자가 B.O 화면에서 본사 직원에게 문의가 가능한 위젯 노출 후 레이어or팝업으로 톡/메일 접수 제공\n' +
      '본사 문의는 다른 센터로 별도 구성\n' +
      '지점 ↔︎ 본사 티켓은 문의한 티켓에 연결';

    await this.getSummary('다음 내용을 요약해줘\n' + question, (text) => {
      console.log(text);
    })
	}
	async getAccessToken() {
		if (KEY_ACCESS_TOKEN) {
			return KEY_ACCESS_TOKEN;
		}

    const resp = await fetch("https://chat.openai.com/api/auth/session")
      .then((r) => r.json())
      .catch(() => ({}));
    if (!resp.accessToken) {
      throw new Error("UNAUTHORIZED");
    }
    KEY_ACCESS_TOKEN = resp.accessToken;
    return resp.accessToken;
	}

	async getConversationId() {
		const accessToken = await this.getAccessToken();
		const resp = await fetch(
			"https://chat.openai.com/backend-api/conversations?offset=0&limit=1",
			{
				method: "GET",
				headers: {
					"Content-Type": "application/json",
					Authorization: `Bearer ${accessToken}`,
				},
			}
		)
			.then((r) => r.json())
			.catch(() => ({}));
		if (resp?.items?.length === 1) {
			return resp.items[0].id;
		}
		return "";
	}

	async getSummary(question, callback) {
		const accessToken = await this.getAccessToken();
		const messageJson = {
			action: "next",
			messages: [
				{
					id: uuidv4(),
					author: {
						role: "user",
					},
					role: "user",
					content: {
						content_type: "text",
						parts: [question],
					},
				},
			],
			model: "text-davinci-002-render",
			parent_message_id: uuidv4(),
		};

		await fetchSSE("https://chat.openai.com/backend-api/conversation", {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
				Authorization: `Bearer ${accessToken}`,
			},
			body: JSON.stringify(messageJson),
			onMessage(message) {
				if (message === "[DONE]") {
					return;
				}
				try {
					const data = JSON.parse(message);
					const text = data.message?.content?.parts?.[0];
					if (text) {
						callback(text);
					}
				} catch (err) {
					console.log("sse message", message);
					console.log(`Error in onMessage: ${err}`);
				}
			},
			onError(err) {
				console.log(`Error in fetchSSE: ${err}`);
			},
		});
	}

}

