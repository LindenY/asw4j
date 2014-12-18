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
  <version>1.0.0-RELEASE</version>
</dependency>
```

## About Logging
asw4j integrates with <a href="http://www.slf4j.org/">SLF4J logging API</a> to provide insightful information during execution. It categories execution messages into three different logging level (trace, debug, and info) to meet the diversity needs of different stages of development. To know more about how to integrate SLF4J, please visit <a href="http://www.slf4j.org/">http://www.slf4j.org/</a>.

## MIT License
Copyright (c) 2014 Desmond Lin

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
