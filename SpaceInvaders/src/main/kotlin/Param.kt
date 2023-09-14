val WIDTH = 1200.0
val HEIGHT = 800.0
enum class EnemyIndex(val value: Int){
    Enemy1(0),
    Enemy2(1),
    Enemy3(2)
}

fun EnemyScore(index : EnemyIndex): Int {
    return when(index){
        EnemyIndex.Enemy1 -> 10
        EnemyIndex.Enemy2 -> 20
        EnemyIndex.Enemy3 -> 30
    }
}

val logo = "file:src/main/assets/images/logo.png"

val backgroundSoundTrack = arrayOf<String>(
    "file:src/main/assets/sounds/fastinvader1.wav",
    "file:src/main/assets/sounds/fastinvader2.wav",
    "file:src/main/assets/sounds/fastinvader3.wav",
    "file:src/main/assets/sounds/fastinvader4.wav",
)
val explosionSoundTrack = "file:src/main/assets/sounds/explosion.wav"
val shootSoundTrack = "file:src/main/assets/sounds/shoot.wav"
const val BGMRate = 15
const val BGMCycle = 4

val enemyImage = arrayOf<Pair<String,String>>(
    Pair("file:src/main/assets/images/enemy1.png","file:src/main/assets/images/bullet1.png"),
    Pair("file:src/main/assets/images/enemy2.png","file:src/main/assets/images/bullet2.png"),
    Pair("file:src/main/assets/images/enemy3.png","file:src/main/assets/images/bullet3.png")
)
val playerImage = Pair("file:src/main/assets/images/player.png","file:src/main/assets/images/player_bullet.png")

const val maxLevel = 3
const val maxLives = 3

const val shipWidth = 80.0
const val shipHeight = 48.0
const val shipMoveSpeed = 10.0

const val enemyWidth = 60.0
const val enemyHeight = 40.0
const val enemySpacing = 5.0

const val enemyInitSpeed = 1.0
const val enemyLevelIncSpeed = 2.0
const val enemyAccelerateSpeed = 0.1

const val missileWidth = 15.0
const val missileHeight = 30.0
const val playerMissileSpeed = 10.0
const val enemyMissileSpeed = 5.0

const val playerMissileRate = 30
const val enemyMissileRate = 60

const val ENEMY3ROW = 1
const val ENEMY2ROW = 2
const val ENEMY1ROW = 2
const val ENEMYCOL = 10

const val statusBarHeight = 40.0

const val gameTextStyle = "-fx-fill: white; -fx-font-size: 30px; "
const val instructionStyle = "-fx-font-size: 20px; "
const val titleBackgroundStyle = "-fx-background-color: #f0f0f0;"
