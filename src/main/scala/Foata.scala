import scala.annotation.tailrec
import Util.addToMap
import Dependencies.isD

object Foata {
  /** Wartość reprezentująca Marker */
  val MARKER: Int = 2

  /** Wartość reprezentująca Symbol */
  val SYMBOL: Int = 1

  /**
   * @param stacks mapa (symbol -> stos)
   * @tparam A typ symbolu
   * @tparam B typ elementów na stosie
   * @return czy wszystkie stosy w mapie są puste
   */
  def allEmpty[A, B](stacks: Map[A, Array[B]]): Boolean =
    stacks.values.forall(_.isEmpty)

  /**
   * @param stack stos
   * @return czy na szczycie stosu jest Marker lub stos jest pusty
   */
  def markerOrEmpty(stack: Array[Int]): Boolean =
    stack.isEmpty || stack(0).equals(MARKER)

  /**
   * Usuwa Markery ze stosów tak długo, jak znajdują się one na wierzchu wszystkich niepustych stosów
   * @param stacks mapa (symbol -> stos)
   * @tparam A typ symboli
   * @return nowa mapa z usuniętymi Markerami
   */
  @tailrec
  def removeMarkers[A](stacks: Map[A, Array[Int]]): Map[A, Array[Int]] =
    if (stacks.values.forall(markerOrEmpty) && !allEmpty(stacks))
      removeMarkers(stacks.keys.filter(!stacks(_).isEmpty).foldLeft(Map[A, Array[Int]]())((m, k) => addToMap(m, k, stacks(k).drop(1))))
    else stacks

  /**
   * Wypisuje wszystkie stosy
   * @param stacks mapa (symbol -> stos)
   * @tparam A typ symboli
   */
  def printStacks[A](stacks: Map[A, Array[Int]]): Unit =
    stacks.values.foreach(v => println(v.mkString("> ", " ", " <")))

  /**
   * Tworzy i wypełnia stosy (zgodnie z algorytmem ze str. 10 "V. Diekert, Y. M ́etivier - Partial commutation and traces, [w:] Handbook of Formal Languages, Springer, 1997")
   * @param alpha tablica symboli alfabetu
   * @param word słowo
   * @param prods mapa (symbol -> produkcja)
   * @tparam A typ symboli w alfabecie
   * @return mapa zawierająca wypełnione stosy
   */
  def fillStacks[A](alpha: Array[A], word: Array[A], prods: Map[A, String]): Map[A, Array[Int]] =
    if (word.isEmpty) alpha.foldLeft(Map[A, Array[Int]]())((m, a) => addToMap(m, a, Array[Int]()))
    else fillStacks(alpha, word.drop(1), prods)
      .foldLeft(Map[A, Array[Int]]())((m, kv: (A, Array[Int])) => addToMap(m, kv._1,
        if (kv._1 == word(0)) kv._2.prepended(SYMBOL) else if (isD(prods, word(0), kv._1)) kv._2.prepended(MARKER) else kv._2
      ))

  /**
   * Analizuje stosy i wyznacza klasy równoważności (zgodnie z algorytmem ze str. 10 "V. Diekert, Y. M ́etivier - Partial commutation and traces, [w:] Handbook of Formal Languages, Springer, 1997")
   * @param stacks mapa zawierająca stosy
   * @return tablica zawierająca poszczególne klasy Foaty
   */
  def readStacks(stacks: Map[String, Array[Int]]): Array[Array[String]] =
    if (allEmpty(stacks)) Array[Array[String]]()
    else
      readStacks(
        removeMarkers(stacks).foldLeft(Map[String, Array[Int]]())(
          (m, kv) => if (markerOrEmpty(kv._2)) addToMap(m, kv._1, kv._2.drop(1)) else addToMap(m, kv._1, kv._2.drop(1))
        )
      ).prepended(removeMarkers(stacks).filter(kv => !markerOrEmpty(kv._2)).keys
        .foldLeft(Array[String]())((a, k) => a.appended(k)))

  /**
   * Uzyskuje klasy Foaty przy użyciu [[fillStacks]] i [[readStacks]]
   * @param alpha alfabet symboli
   * @param word słowo symboli z alfabetu
   * @param prods mapa (symbol -> produkcja)
   * @return
  tablica zawierająca poszczególne klasy Foaty
   */
  def getFoata(alpha: Array[String], word: Array[String], prods: Map[String, String]): Array[Array[String]] =
    readStacks(fillStacks(alpha, word, prods))

  /**
   * Uzyskuje klasy Foaty z grafu Diekerta
   * @param word słowo symboli z alfabetu
   * @param graph graf w formie zgodnej z [[Diekert.graph]]
   * @return tablica zawierająca poszczególne klasy Foaty
   */
  def foataFromGraph(word: Array[String], graph: Array[(Int, Int, Array[Int], Array[Int])]): Array[Array[String]] =
    graph.map(t => t._2).distinct.sorted.foldLeft(Array[Array[String]]())((a, i) => a.appended(
      graph.filter(t => t._2 == i).map(t => word(t._1)).foldLeft(Array[String]())((a, w) => a.appended(w))
    ))

  /**
   * Tworzy `String` na podstawie klas Foaty
   * @param foata tablica zawierająca poszczególne klasy Foaty
   * @return wersja tekstowa klas Foaty, gotowa do wydruku
   */
  def printFoata(foata: Array[Array[String]]): String =
    foata.foldLeft("")(_ + _.mkString("[", ", ", "]"))

  /**
   * Wyznacza klasy Foaty i tworzy `String` na ich podstawie
   * @param alpha tablica symboli alfabetu
   * @param word słowo symboli z alfabetu
   * @param prods mapa (symbol -> produkcja)
   * @return wersja tekstowa klas Foaty, gotowa do wydruku
   */
  def printFoata(alpha: Array[String], word: Array[String], prods: Map[String, String]): String =
    printFoata(getFoata(alpha, word, prods))
}
