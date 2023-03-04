import kotlin.random.Random

// Function used to generate a random goal state (random four-digit number)
fun createRandomGoalState(): HashMap<Int, Int> {
    // Initialise the HashMap
    val goalState: HashMap<Int, Int> = HashMap()
    // function to create a random number stored in variable
    val randomNumber: () -> (Int) = {Random.nextInt(0, 9)}
    // position of the newly added number
    var position = 0
    // As long as the goal stat has less than four digits
    while (goalState.size != 4){
        // get a random number
        val number = randomNumber()
        // check for duplicates
        if (!goalState.containsValue(number)) {
            // if random number chosen is not a duplicate
            // add it into goal state
            goalState[position] = number
            // increase the position
            position++
        }
    }
    // after filling the goal state with four digits return it
    return goalState
}

// function used to read the four-digit number the user enters
fun getUserInput(): HashMap<Int, Int>{
    // Initialise the HashMap
    val userState: HashMap<Int, Int> = HashMap()
    // function to validate the number entered by the user stored in variable
    val checkUser: (Int) -> Boolean = {number: Int -> number < 10000}
    // as long as the user didn't choose a valid four-digit number
    while (userState.size < 4){
        // print
        println("Please enter your number (4 digits): ")
        // get the number the user entered
        var number = Integer.valueOf(readLine())
        // check if it's a four-digit number
        if (checkUser(number)){
            // in reverse order save it into the user state
            for (i in 3 downTo 0){
                // to save each digit by itself divide the number with modulo 10
                userState[i] = number%10
                // to move on to the next number dive the number by 10 to lose the last digit
                number /= 10
            }
        } else {
            // if the user didn't enter a four-digit number
            println("Only enter a four-digit number")
        }
    }
    // return the filled user state
    return userState
}

// Function to check whether the numbers the user entered are on the right position or at least occur in the goal state
fun checkPositionsOfNumbers(goal: HashMap<Int, Int>, user: HashMap<Int,Int>, check: HashMap <Int, Int>) {
    // Initialise array to avoid that a number is checked more than once
    val numberCheck: HashMap<Int, Int> = HashMap()
    // As long as both HashMap go
    for (i in 0..3){
        // If number is already on right position
        if (goal[i] == user[i]){
            // mark it in the checkMap as 1 (number is on correct position - green)
            check[i] = 1
        } else {
            // If number is not on right position
            // check if number has already been checked
            if (!numberCheck.containsValue(user[i])) {
                // If number occurs somewhere in the goal state
                if (goal.containsValue(user[i])) {
                    // mark it in the checkMap as 2 (number occurs in goal state but not on correct position - yellow)
                    check[i] = 2
                    // add number to the numberCheckMap, so it doesn't get checked more than once
                    user[i]?.let { numberCheck.put(i, it) }
                }
            }
        }
    }
}

// Function to reset the CheckMap each time the User enters a new four-digit number
fun resetCheckState(): HashMap<Int, Int>{
    // Initialise the HashMap
    val checkState: HashMap<Int, Int> = HashMap()
    // As long as HashMap goes
    for (i in 0..3){
        // set all positions to default 3 (number doesn't occur in goal state - red)
        checkState[i] = 3
    }
    // return filled checkMap
    return checkState
}

// Function used to print the numbers in color correlating to the user input and the goal state
fun printNumbers(user: HashMap<Int, Int>, check: HashMap<Int, Int>){
    // variable for ANSI-Color code
    val red = "\u001b[31m"
    val green = "\u001B[32m"
    val yellow = "\u001B[93m"
    val reset = "\u001b[0m"
    // for each entry in HashMaps
    for (i in 0..3){
        // check with value each position has
        when (check[i]){
            // If 1 number appears green
            1 -> print(green + user[i] + reset + " ")
            // If 2 number appears yellow
            2 -> print(yellow + user[i] + reset + " ")
            // If 3 number appears red
            3 -> print(red + user[i] + reset + " ")
            // else only print number
            else -> print(user[i])
        }
    }
    // print empty line afterwards
    println()
}

// Function to print a Welcome Text
fun welcomeText(){
    // variable for ANSI-Color code
    val red = "\u001b[31m"
    val green = "\u001B[32m"
    val yellow = "\u001B[93m"
    val reset = "\u001b[0m"
    // Welcome Text
    println("Welcome to the Number-Guessing-Game!")
    println("You need to guess a random generated 4-digit number (0-9).")
    println(green + "Green" + reset + " - Digit is on the correct position")
    println(yellow + "Yellow" + reset + " - Digit is part of the generated number but not on the correct position")
    println(red + "Red" + reset + " - Digit is not part of the generated number")
}

// Function to start the Game
fun playGame() {
    // print Welcome Text
    welcomeText()
    // Initialise HashMaps
    // goalState is used for the random chosen four-digit number the user needs to guess
    val goalState : HashMap<Int, Int> = createRandomGoalState()
    // userState is used for the four-digit number the user enters when guessing
    var userState: HashMap<Int, Int> = HashMap()
    // checkState is used to check whether the digits on each position are in the correct position (1), occur in the goal state (2) or don't occur in the goal state (3)
    var checkState: HashMap<Int, Int>
    // variable to count the tries the user needs to guess the correct number
    var tries = 0
    // as long as the number the users guesses and the random chosen one are not the same
    while (goalState != userState){
        // reset the checkState
        checkState = resetCheckState()
        // ask user for input
        userState = getUserInput()
        // check position of all numbers and see whether they are on the right position, occur in goal state or don't occur
        checkPositionsOfNumbers(goalState, userState, checkState)
        // print the numbers the user entered in color according to checkState
        printNumbers(userState, checkState)
        // increase the number of tries
        tries++
    }
    // if both numbers match
    // print Stats
    println("You won Congratulations!")
    print("Generated Number: ")
    goalState.forEach { (key, value) -> print("$value ") }
    println("\nTries needed: $tries")

}

// main function
fun main() {
    // start the game
    playGame()
}