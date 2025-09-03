package net.mysterria.archive.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @GetMapping("/")
    @ResponseBody
    public String helloWorld() {
        return "Hello World!";
    }

    @GetMapping("/developers/ikeepcalm")
    @ResponseBody
    public String ikeepcalmPage() {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>ikeepcalm - Bohdan Horokh</title>
                <style>
                    body { font-family: Arial, sans-serif; margin: 40px; background-color: #f5f5f5; }
                    .container { max-width: 800px; margin: 0 auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                    h1 { color: #333; border-bottom: 3px solid #007acc; padding-bottom: 10px; }
                    .profile { display: flex; align-items: center; margin: 20px 0; }
                    .info { margin-left: 20px; }
                    .github { color: #007acc; text-decoration: none; }
                    .github:hover { text-decoration: underline; }
                </style>
            </head>
            <body>
                <div class="container">
                    <h1>Developer Profile</h1>
                    <div class="profile">
                        <div class="info">
                            <h2>ikeepcalm</h2>
                            <h3>Bohdan Horokh</h3>
                            <p><strong>Role:</strong> Project Developer</p>
                            <p><strong>GitHub:</strong> <a href="https://github.com/ikeepcalm" class="github">@ikeepcalm</a></p>
                            <p><strong>Project:</strong> Mysterria Archive</p>
                        </div>
                    </div>
                </div>
            </body>
            </html>
            """;
    }

    @GetMapping("/developers/djecka")
    @ResponseBody
    public String djecka1337Page() {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Djecka1337 - Yevhen Svyacheniy</title>
                <style>
                    body { font-family: Arial, sans-serif; margin: 40px; background-color: #f5f5f5; }
                    .container { max-width: 800px; margin: 0 auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                    h1 { color: #333; border-bottom: 3px solid #28a745; padding-bottom: 10px; }
                    .profile { display: flex; align-items: center; margin: 20px 0; }
                    .info { margin-left: 20px; }
                    .github { color: #28a745; text-decoration: none; }
                    .github:hover { text-decoration: underline; }
                </style>
            </head>
            <body>
                <div class="container">
                    <h1>Developer Profile</h1>
                    <div class="profile">
                        <div class="info">
                            <h2>Djecka1337</h2>
                            <h3>Yevhen Svyacheniy</h3>
                            <p><strong>Role:</strong> Project Developer</p>
                            <p><strong>GitHub:</strong> <a href="https://github.com/Djecka1337" class="github">@Djecka1337</a></p>
                            <p><strong>Project:</strong> Mysterria Archive</p>
                        </div>
                    </div>
                </div>
            </body>
            </html>
            """;
    }

    @GetMapping("/developers/esfer")
    @ResponseBody
    public String esferPage() {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Esfer Useinov</title>
                <style>
                    body { font-family: Arial, sans-serif; margin: 40px; background-color: #f5f5f5; }
                    .container { max-width: 800px; margin: 0 auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                    h1 { color: #333; border-bottom: 3px solid #dc3545; padding-bottom: 10px; }
                    .profile { display: flex; align-items: center; margin: 20px 0; }
                    .info { margin-left: 20px; }
                    .github { color: #dc3545; text-decoration: none; }
                    .github:hover { text-decoration: underline; }
                </style>
            </head>
            <body>
                <div class="container">
                    <h1>Developer Profile</h1>
                    <div class="profile">
                        <div class="info">
                            <h2>Esfer Useinov</h2>
                            <p><strong>Role:</strong> Project Developer</p>
                            <p><strong>Project:</strong> Mysterria Archive</p>
                        </div>
                    </div>
                </div>
            </body>
            </html>
            """;
    }
}
