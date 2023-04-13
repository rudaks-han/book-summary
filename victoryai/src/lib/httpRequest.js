export default class HttpRequest {

    static async request(url, options) {
        return await this.fetchWithTimeout(url, options)
            .then(response => {
                /*console.log('HttpRequest#request')
                console.log(response)*/
                if (!response.ok) {
                    return response;
                }
                return response;
            })
            .then(response => response.json())
            .catch(err => {
                console.error(`[httpRequest Error] ${url} : ${err.message}`)
                return null;
            });
    }

    static async requestText(url) {
        return await fetch(url)
            .then(response => {
                if (!response.ok) {
                    return response;
                }
                return response;
            }).then(response => response)
            .catch(err => {
                console.error(`[httpRequest Error] ${url} : ${err.message}`)
                return null;
            });
    }

    static async fetchWithTimeout(url, options = {}) {
        const { timeout = 8000 } = options;

        const controller = new AbortController();
        const id = setTimeout(() => controller.abort(), timeout);
        const response = await fetch(url, {
            ...options,
            signal: controller.signal
        });
        clearTimeout(id);
        return response;
    }

    static async requestAll(urls, options) {
        const _this = this;
        return Promise.all(_this.toPromise(urls, options))
            .then(responses => {
                return responses
            }).catch(e => e.response);
    }

    static toPromise(urls, options) {
        const promises = [];
        urls.forEach(url => {
            promises.push(
                this.request(url, options)
                /*fetch(url, options)
                    .then(response => response.json())
                    .catch(e => {
                        throw e;
                    })*/
            );
        })

        return promises;
    }
}
