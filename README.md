# asw4j

asw4j(Auto Spread Work for Java) is a Java library aiming to take advantage of multi-core processors and remove the difficulty of the concurrent programming. 

## About asw4j
When comes to concurrency programing, it can be overwhelming from a variety of problems such as race condition, dead lock, data sync, and so on. So, here comes the asw4j, a friendly, lightweight, and independent Java library which is built to solve common concurrency programing problems. The idea of asw4j is that by seprating the business logic from concurrency logic, which is logic like locking and syncing data, and leaving the concurrency operation managed by asw4j, developers can care less about concurrency problems and focus more on their actual work.

## Structure
There are these main components in asw4j:
<ul>
<li>Instruction: works to be executed by workers</li>
<li>WorkerManager: manages workers(Threads) to work on Instructions</li>
<li>InstructionResolver: resolve available Instructions and inject the require data for these Instructions</li>
<li>DataStore: store and search for data</li>
</ul>

## Download
asw4j is deployed in <a href="http://search.maven.org/">Maven Central Repo</a>. To download, just include the following in pom.xml file.
```xml
<dependency>
  <groupId>ca.uwaterloo</groupId>
  <artifactId>asw4j</artifactId>
  <version>{asw4j.version}</version>
</dependency>
```

## About Logging
asw4j integrates with <a href="http://www.slf4j.org/">SLF4J logging API</a> to provide insightful information during execution. It categories execution messages into three different logging level (trace, debug, and info) to meet the diversity needs of different stages of development. To know more about how to integrate SLF4J, please visit <a href="http://www.slf4j.org/">http://www.slf4j.org/</a>.

## MIT License
Copyright (c) 2014 Desmond Lin

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

## Sample Code
This snippet of Java code demonstrates the basic usage of asw4j. It loops through every integer between the range, and find the Palindrome number (Palindrome number: a number which reads the same backward or forward.).
```java
public class Main {

	public static void main(String[] args) {

		Integer[] initData = new Integer[] { 0,  50000000};

		DataStore dataStore = new ConcurrentMapDataStore();
		dataStore.registerBalancer(TypeToken.get(data.getClass()), new Mapper());
		dataStore.registerCombiner(TypeToken.get(ArrayList.class), new CollectionCombiner());
		dataStore.add(initData);

		InstructionResolver instructionResolver = new SimpleInstructionResolver(dataStore);
		instructionResolver.registerInstructionClass(PalindromeFindingInstruction.class);

		WorkerManager workerManager = new ThreadPoolWorkerManager(3, 5, instructionResolver);
		List resultList = workerManager.start(ArrayList.class, null);
	}

	public static class PalindromeFindingInstruction extends
			Instruction<Integer[], List<String>> {

		@Override
		public List<String> execute(Integer[] requireData) {
			List<String> strList = new ArrayList<String>();
			for (int i = requireData[0]; i < requireData[1]; i++) {	
				String str = String.valueOf(i);
				int length = str.length() - 1;

				boolean palindrome = true;
				for (int j = 0; j <= Math.floor(length / 2); j++) {
					if (str.charAt(j) != str.charAt(length - j)) {
						palindrome = false;
						break;
					}
				}

				if (palindrome) {
					strList.add(str);
				}
			}
			return strList;
		}
	}

	public static class Mapper implements Balancer<Integer[]> {

		@Override
		public Collection<Integer[]> balance(Collection<Integer[]> collection) {
			Integer[] data = null;
			for (Integer[] d : collection) {
				data = d;
			}

			List<Integer[]> resultCollection = new ArrayList<Integer[]>();
			int period = (int) Math.ceil((data[1] - data[0]) / 3);
			int start = data[0];

			while (start < data[1]) {
				Integer[] snippet = new Integer[2];
				snippet[0] = start;
				snippet[1] = Math.min(start + period, data[1]);
				resultCollection.add(snippet);
				start += period;
			}

			return resultCollection;
		}
	}

	public static class CollectionCombiner implements Combiner<List<String>> {

		@Override
		public List<String> combine(Collection<List<String>> collection) {
			List<String> stringList = new ArrayList<String>();

			for (List<String> strs : collection) {
				stringList.addAll(strs);
			}

			return stringList;
		}
	}
}
