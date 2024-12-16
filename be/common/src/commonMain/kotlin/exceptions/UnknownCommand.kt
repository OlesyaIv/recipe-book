package exceptions

import models.RecipeBookCommand

class UnknownCommand(command: RecipeBookCommand) : Throwable("Wrong command $command at mapping toTransport stage")
