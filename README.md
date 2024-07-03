# TelegramBot

It's a Telegram bot built using Spring Boot that integrates with OpenAI's ChatGPT to provide intelligent conversation capabilities. It supports commands for interacting with ChatGPT and managing chat history.

## Prerequisites
- Java 11 or higher
- Maven
- Docker
- Telegram account to create a bot via BotFather
- OpenAI API key

## Getting Started
### 1. Create a Telegram Bot
   - Open Telegram and search for [BotFather](https://web.telegram.org/k/#@BotFather).
   - Use the /newbot command to create a new bot.
   - Follow the prompts to name your bot and get the bot token.

### 2. Create an OpenAI Api Key
- Go to [Platform OpenAI](https://platform.openai.com).
- Use "Create new secret key" in the API keys section.
- Copy the key and save in a safe place.
  - **You won't be allowed to see the key in future.**

### 3. Clone the Repository
```bash
git clone https://github.com/inesagh/telegrambot.git
cd telegrambot
```
### 4. Configure the Application
   Create an application.properties file in the src/main/resources directory with the following content:
```properties
bot.name=<your_bot_name>
bot.token=<your_bot_token>
openai.gpt.model=gpt-3.5-turbo
openai.gpt.url=https://api.openai.com/v1/chat/completions
openai.gpt.key=<your_openai_key>
```
Replace your_bot_name, your_bot_token, and the OpenAI API URL with your actual bot name, bot token, and the OpenAI endpoint.

### 5. Build the Project
```bash
mvn clean install
```

### 6. Build the Docker Image from the **root** directory:
```bash
docker build -t bot-image .
```

### 7. Run the Docker Container:
```bash
docker run -d --name bot-container -p 8080:8080 bot-image
```


### 6. Interact with Your Bot /Bot Commands/
- Open Telegram and search for your bot by its name.
- Use /start to get a welcome message.
- Use /chatgpt Your message here to interact with ChatGPT.
- Use /clear to clear the chat history.


