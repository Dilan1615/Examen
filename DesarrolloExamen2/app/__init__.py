from flask import Flask

def create_app():
    app = Flask(__name__)

    from app.routes.route import router
    app.register_blueprint(router)

    return app
