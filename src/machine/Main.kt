package machine

enum class CoffeeType(val water: Int, val milk: Int, val beans: Int, val cost: Int) {
    ESPRESSO(water = 250, milk = 0, beans = 16, cost = 4),
    LATTE(water = 350, milk = 75, beans = 20, cost = 7),
    CAPPUCCINO(water = 200, milk = 100, beans = 12, cost = 6)
}

enum class CoffeeMachineState(val commandText: String) {
    READ_ACTION(commandText = "Write action (buy, fill, take, remaining, exit): "),
    BUY(commandText = "What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: "),
    ADD_WATER(commandText = "Write how many ml of water do you want to add: "),
    ADD_MILK(commandText = "Write how many ml of milk do you want to add: "),
    ADD_BEANS(commandText = "Write how many grams of coffee beans do you want to add: "),
    ADD_CUPS(commandText = "Write how many disposable cups of coffee do you want to add: "),
    NULL(commandText = "")
}

class CoffeeMachine(
    private var water: Int = 400,
    private var milk: Int = 540,
    private var beans: Int = 120,
    private var cups: Int = 9,
    private var money: Int = 550) {

    var state: CoffeeMachineState = CoffeeMachineState.READ_ACTION
    var isInUse: Boolean = true

    fun handleCommand(_command: String) {
        when (this.state) {
            CoffeeMachineState.READ_ACTION -> {
                when (_command) {
                    "buy" -> this.state = CoffeeMachineState.BUY
                    "fill" -> this.state = CoffeeMachineState.ADD_WATER
                    "remaining" -> displayRemainingSupplies()
                    "take" -> takeMoney()
                    "exit" -> this.isInUse = false
                    else -> println("Unknown command! Please re-enter.")
                }
            }
            CoffeeMachineState.BUY -> {
                when (_command) {
                    "1" -> buyCoffee(CoffeeType.ESPRESSO)
                    "2" -> buyCoffee(CoffeeType.LATTE)
                    "3" -> buyCoffee(CoffeeType.CAPPUCCINO)
                    "back" -> this.state = CoffeeMachineState.READ_ACTION
                    else -> println("Unknown command! Please re-enter.")
                }
            }
            CoffeeMachineState.ADD_WATER -> {
                this.water += _command.toInt()
                this.state = CoffeeMachineState.ADD_MILK
            }
            CoffeeMachineState.ADD_MILK -> {
                this.milk += _command.toInt()
                this.state = CoffeeMachineState.ADD_BEANS
            }
            CoffeeMachineState.ADD_BEANS -> {
                this.beans += _command.toInt()
                this.state = CoffeeMachineState.ADD_CUPS
            }
            CoffeeMachineState.ADD_CUPS -> {
                this.cups += _command.toInt()
                this.state = CoffeeMachineState.READ_ACTION
            }
            CoffeeMachineState.NULL -> this.state = CoffeeMachineState.READ_ACTION
        }
    }

    private fun displayRemainingSupplies() {
        println()
        println("The coffee machine has: ")
        println("${this.water} of water")
        println("${this.milk} of milk")
        println("${this.beans} of coffee beans")
        println("${this.cups} of disposable cups")
        println("$${this.money} of money")
    }

    private fun takeMoney() {
        println("I gave you $${this.money}")
        this.money = 0
    }

    private fun buyCoffee(_coffeeType: CoffeeType) {
        if (this.checkSupplies(_coffeeType)) {
            this.water -= _coffeeType.water
            this.milk -= _coffeeType.milk
            this.beans -= _coffeeType.beans
            this.cups -= 1
            this.money += _coffeeType.cost
        }
        this.state = CoffeeMachineState.READ_ACTION
    }
    
    private fun checkSupplies(_coffeeType: CoffeeType, _cups: Int = 1): Boolean {
        when {
            this.water < _coffeeType.water -> {
                println("Sorry, not enough water!")
            }
            this.milk < _coffeeType.milk -> {
                println("Sorry, not enough milk!")
            }
            this.beans < _coffeeType.beans -> {
                println("Sorry, not enough coffee beans!")
            }
            this.cups < _cups -> {
                println("Sorry, not enough disposable cups!")
            }
            else -> {
                println("I have enough resources, making you a coffee!")
                return true
            }
        }
        return false
    }
}

fun main() {
    val coffeeMachine = CoffeeMachine()
    while (coffeeMachine.isInUse) {
        println()
        print(coffeeMachine.state.commandText)
        val command = readLine()!!.trim()
        coffeeMachine.handleCommand(command)
    }
}